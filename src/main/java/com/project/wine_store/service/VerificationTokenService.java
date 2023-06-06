package com.project.wine_store.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.wine_store.model.Client;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VerificationTokenService {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;
    @Value("{jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiryInSeconds}")
    private int expiryInSeconds;
    @Value("${jwt.resetPasswordExpiryInSeconds}")
    private int resetPasswordExpiryInSeconds;
    private Algorithm algorithm;

    @PostConstruct
    public void postConstruct() {
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    // JWT pentru client, la login
    public String generateJWT(Client client) {
        return JWT.create()
                .withClaim("USERNAME", client.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * expiryInSeconds)))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    // JWT pentru verificarea email-ului
    public String generateEmailVerificationJWT(Client client) {
        return JWT.create()
                .withClaim("VERIFICATION_EMAIL", client.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * expiryInSeconds)))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    // scoatem username-ul din token-ul generat
    public String getUsername(String token) {
        return JWT.decode(token).getClaim("USERNAME").asString();
    }

    // JWT pentru parola uitata
    public String generatePasswordResetJWT(Client client) {
        return JWT.create()
                .withClaim("RESET_PASSWORD_EMAIL", client.getEmail())
                // expira in 30 min
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * resetPasswordExpiryInSeconds)))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    // scoatem email-ul din token
    public String getResetPassEmail(String token) {
        return JWT.decode(token).getClaim("RESET_PASSWORD_EMAIL").asString();
    }

}
