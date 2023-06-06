package com.project.wine_store.api.controller;

import com.project.wine_store.model.Address;
import com.project.wine_store.model.Client;
import com.project.wine_store.repository.AddressRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientController {

    private AddressRepository addressRepository;

    public ClientController(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    // Afiseaza lista de adrese a clientului care este logat
    @GetMapping("/{clientID}/address")
    public ResponseEntity<List<Address>> getClientAddress(@AuthenticationPrincipal Client client, @PathVariable Long clientID) {
        // daca clientul nu are permisiunea sa vada adresa
        if (!doesClientHavePermission(client, clientID)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(addressRepository.findByClient_Id(clientID));
    }

    private boolean doesClientHavePermission(Client client, Long clientID) {
        return client.getId() == clientID;
    }

    // Adaugam o noua adresa
    @PutMapping("/{clientID}/address")
    public ResponseEntity<Address> addAddress(@AuthenticationPrincipal Client client, @PathVariable Long clientID, @RequestBody Address address) {
        // Are clientul permisiunea sa schimbe adresa?
        if (!doesClientHavePermission(client, clientID)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        address.setId(null);
        // in caz ca se adauga o adresa de catre un admin
        Client tempClient = new Client();
        tempClient.setId(clientID);
        address.setClient(tempClient);
        return ResponseEntity.ok(addressRepository.save(address));
    }

    // Editam o adresa
    @PatchMapping("/{clientID}/address/{addressID}")
    public ResponseEntity<Address> updateAddress(@AuthenticationPrincipal Client client, @PathVariable Long clientID, @PathVariable Long addressID, @RequestBody Address address) {
        // Are clientul permisiunea sa schimbe adresa?
        if (!doesClientHavePermission(client, clientID)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // verificam daca addressID din url e aceasi cu cea din addressBody
        if (address.getId() == addressID) {
            // adresa exista
            Optional<Address> addressOptional = addressRepository.findById(addressID);
            if (addressOptional.isPresent()) {
                //addressID apartine clientului care e logged in
                if (addressOptional.get().getClient().getId() == clientID) {
                    address.setClient(addressOptional.get().getClient());
                    return ResponseEntity.ok(addressRepository.save(address));
                }
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{clientID}/address")
    public ResponseEntity deleteAddress(@AuthenticationPrincipal Client client, @PathVariable Long clientID, @RequestParam Long addressID) {
        // Are clientul permisiunea sa stearga adresa?
        if (!doesClientHavePermission(client, clientID)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // verificam daca addressID exista
        Optional<Address> addressOptional = addressRepository.findById(addressID);
        if (addressOptional.isPresent()) {
            //addressID apartine clientului care e logged in
            if (addressOptional.get().getClient().getId() == clientID) {
                addressRepository.deleteById(addressID);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }


}
