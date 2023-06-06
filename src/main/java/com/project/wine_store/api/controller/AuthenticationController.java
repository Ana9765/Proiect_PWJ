package com.project.wine_store.api.controller;

import com.project.wine_store.api.model.LoginBody;
import com.project.wine_store.api.model.LoginResponse;
import com.project.wine_store.api.model.PasswordResetBody;
import com.project.wine_store.api.model.RegistrationBody;
import com.project.wine_store.exception.CantSendEmailException;
import com.project.wine_store.exception.ClientAlreadyExistsException;
import com.project.wine_store.exception.ClientEmailNotVerifiedException;
import com.project.wine_store.exception.EmailNotFoundException;
import com.project.wine_store.model.Client;
import com.project.wine_store.repository.ClientRepository;
import com.project.wine_store.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private ClientService clientService;
    private final ClientRepository clientRepository;

    public AuthenticationController(ClientService clientService,
                                    ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    // Inregistreaza un client nou
    @PostMapping("/register")
    public ResponseEntity registerClient(@Valid @RequestBody RegistrationBody registrationBody) {
        try {
            clientService.registerClient(registrationBody);
            return ResponseEntity.ok().build();  // code 200 - ok
        } catch (ClientAlreadyExistsException e) { // clientul deja exista
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (CantSendEmailException e) {     // nu s-a putut trimite email-ul
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginClient(@Valid @RequestBody LoginBody loginBody) {
        String jwtToken = null;
        try {
            // se creaza un jwt token pentru sesiunea de login
            jwtToken = clientService.loginClient(loginBody);
        } catch (ClientEmailNotVerifiedException e) {
            // daca email-ul clientului nu este verificat
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setSuccess(false);
            String failureReason = "EMAIL_USER_NOT_VERIFIED";
            // daca a fost retrimis un email de verificare
            if (e.isNewEmailSent()) {
                failureReason = failureReason + "_EMAIL_RESENT";
            }
            loginResponse.setFailureReason(failureReason);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(loginResponse);
        } catch (CantSendEmailException e) {
            // daca nu se poate trimite email-ul, nu e vina clientului
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        if (jwtToken != null) {
            LoginResponse response = new LoginResponse();
            response.setJwtToken(jwtToken);
            response.setSuccess(true); // totul a decurs cum trebuie
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Verification endpoint - cum activeaza clientul contul
    @PostMapping("/verify")
    public ResponseEntity verifyClientEmail(@RequestParam String jwtToken) {
        if (clientService.verifyClient(jwtToken)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/me")
    public Client getLoggedInProfile(@AuthenticationPrincipal Client client) {
        return client;
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity forgotPassword(@RequestParam String email) {
        // incercam sa trimitem email-ul de verificare
        try {
            clientService.forgotPassword(email);
            return ResponseEntity.ok().build();
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (CantSendEmailException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(@Valid @RequestBody PasswordResetBody passwordResetBody) {
        clientService.resetPassword(passwordResetBody);
        return ResponseEntity.ok().build();
    }

    private boolean doesClientHavePermission(Client client, Long clientID) {
        return client.getId() == clientID;
    }

    @DeleteMapping("/{clientID}/delete")
    public ResponseEntity deleteClient (@AuthenticationPrincipal Client client, @RequestParam Long clientID) {
        // Are clientul logat permisiunea sa stearga un client?
        if (!doesClientHavePermission(client, clientID)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // verificam daca clientID exista
        Optional<Client> clientOptional = clientRepository.findById(clientID);
        if (clientOptional.isPresent()) {
            clientRepository.deleteById(clientID);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
