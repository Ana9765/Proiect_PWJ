package com.project.wine_store.api.security;

import com.project.wine_store.model.Client;
import com.project.wine_store.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class ClientDetailsService implements UserDetailsService {

    @Autowired
    ClientRepository clientRepository;
    public Client loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Client> optionalClient = clientRepository.findByUsernameIgnoreCase(username);
        if (optionalClient.isPresent()) {
            System.out.println(username);
            return optionalClient.get();
        }
        return null;
    }

}
