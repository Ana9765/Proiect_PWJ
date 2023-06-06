package com.project.wine_store.service;

import com.project.wine_store.api.model.LoginBody;
import com.project.wine_store.api.model.PasswordResetBody;
import com.project.wine_store.api.model.RegistrationBody;
import com.project.wine_store.exception.CantSendEmailException;
import com.project.wine_store.exception.ClientAlreadyExistsException;
import com.project.wine_store.exception.ClientEmailNotVerifiedException;
import com.project.wine_store.exception.EmailNotFoundException;
import com.project.wine_store.model.Client;
import com.project.wine_store.model.EmailVerificationToken;
import com.project.wine_store.repository.ClientRepository;
import com.project.wine_store.repository.EmailVerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private ClientRepository clientRepository;
    private EncryptionService encryptionService;
    private VerificationTokenService verificationTokenService;
    private EmailService emailService;
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    public ClientService(ClientRepository clientRepository, EncryptionService encryptionService, VerificationTokenService verificationTokenService, EmailService emailService, EmailVerificationTokenRepository emailVerificationTokenRepository) {
        this.clientRepository = clientRepository;
        this.encryptionService = encryptionService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
    }

    // Inregistram client nou
    public Client registerClient(RegistrationBody registrationBody) throws ClientAlreadyExistsException, CantSendEmailException {
        // Daca avem acelasi username sau acelasi email
        if (clientRepository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent() || clientRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()) {
            throw new ClientAlreadyExistsException();
        }
        // Inregistram datele clientului nou
        Client client = new Client();
        client.setEmail(registrationBody.getEmail());
        client.setFirstName(registrationBody.getFirstName());
        client.setLastName(registrationBody.getLastName());
        client.setUsername(registrationBody.getUsername());
        client.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        //generam un email verification token
        EmailVerificationToken emailVerificationToken = createEmailVerificationToken(client);
        // trimitem email-ul de verificare
        emailService.sendVerificationEmail(emailVerificationToken);
        // salvam token-ul de verificare
        emailVerificationTokenRepository.save(emailVerificationToken);
        // client = clientRepository.save(client);
        return clientRepository.save(client);
    }

    // generam un jwt token pentru login, pentru client
    private EmailVerificationToken createEmailVerificationToken(Client client) {
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken(verificationTokenService.generateEmailVerificationJWT(client));
        emailVerificationToken.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        emailVerificationToken.setClient(client);
        client.getEmailVerificationTokens().add(emailVerificationToken);
        return emailVerificationToken;
    }

    // Autentifica clientul
    public String loginClient(LoginBody loginBody) throws ClientEmailNotVerifiedException, CantSendEmailException {
        Optional<Client> optionalClient = clientRepository.findByUsernameIgnoreCase(loginBody.getUsername());
        // clientul exista
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            // parola este ok
            if(encryptionService.verifyPassword(loginBody.getPassword(), client.getPassword())) {
                // daca email-ul a fost verificat, genereaza token
                if (client.getIsEmailVerified()) {
                    return verificationTokenService.generateJWT(client);
                } else {
                    // luam toate token-urile trimise clientului
                    List<EmailVerificationToken> verificationTokens = client.getEmailVerificationTokens();
                    // daca nu exista un token sau a trecut o zi de la ultimul token trimis
                    boolean needToResendEmail = verificationTokens.size() == 0 || verificationTokens.get(0).getCreatedAt().before(new Timestamp(System.currentTimeMillis() - (1440 * 60 * 1000)));
                    // daca trebuie sa retrimitem email
                    if (needToResendEmail) {
                        // generam alt token
                        EmailVerificationToken emailVerificationToken = createEmailVerificationToken(client);
                        // il salvam
                        emailVerificationTokenRepository.save(emailVerificationToken);
                        // trimitem email
                        emailService.sendVerificationEmail(emailVerificationToken);
                    }
                    throw new ClientEmailNotVerifiedException(needToResendEmail);
                }
            }
        }
        return null;
    }

    // verifica daca token-ul de verificare este sau nu valid/verificat deja
    @Transactional
    public boolean verifyClient(String token) {
        // verifica daca token-ul exista
        Optional<EmailVerificationToken> optionalEmailVerificationToken = emailVerificationTokenRepository.findByToken(token);
        if (optionalEmailVerificationToken.isPresent()) {
            EmailVerificationToken emailVerificationToken = optionalEmailVerificationToken.get();
            // luam clientul din token-ul de verificare
            Client client = emailVerificationToken.getClient();
            // daca email-ul nu e verificat
            if (!client.getIsEmailVerified()) {
                client.setIsEmailVerified(true);
                clientRepository.save(client);
                return true;
            }
        }
        return false;
    }

    // Cerem email pentru schimbarea parolei
    public void forgotPassword(String email) throws EmailNotFoundException, CantSendEmailException {
        // luam clientul care si-a uitat parola dupa email
        Optional<Client> optionalClient = clientRepository.findByEmailIgnoreCase(email);
        // daca exista
        if(optionalClient.isPresent()) {
            Client client = optionalClient.get();
            // generam jwt pentru resetare parola
            String token = verificationTokenService.generatePasswordResetJWT(client);
            // trimitem pe mail
            emailService.sendResetPassEmail(client, token);
        } else {
            throw new EmailNotFoundException();
        }
    }

    // Schimbam parola
    public void resetPassword(PasswordResetBody passwordResetBody) {
        // luam email-ul din token
        String email = verificationTokenService.getResetPassEmail(passwordResetBody.getResetPasswordToken());
        // luam clientul asociat cu email-ul
        Optional<Client> optionalClient = clientRepository.findByEmailIgnoreCase(email);
        // daca exista
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            // retinem parola noua
            client.setPassword(encryptionService.encryptPassword(passwordResetBody.getNewPassword()));
            clientRepository.save(client);
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
