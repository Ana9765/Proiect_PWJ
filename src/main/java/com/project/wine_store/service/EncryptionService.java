package com.project.wine_store.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    @Value("${salt.rounds.number}")
    private int numberOfSaltRounds;
    private String salt;

    @PostConstruct
    public void postConstruct() {
        salt = BCrypt.gensalt(numberOfSaltRounds);
    }

    //
    public String encryptPassword(String password) {
        return BCrypt.hashpw(password, salt);
    }

    //verifica daca parola e corecta
    public boolean verifyPassword(String password, String encryptedPassword) {
        return BCrypt.checkpw(password, encryptedPassword);
    }

}
