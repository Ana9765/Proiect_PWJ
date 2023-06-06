package com.project.wine_store.service;

import com.project.wine_store.model.Client;
import com.project.wine_store.repository.ClientRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class VerificationTokenServiceTest {

    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private ClientRepository clientRepository;

    @Before
    public void initClientA() {
        Client client = new Client();
        client.setUsername("ClientA");
        client.setEmail("ClientA@junit.com");
        client.setFirstName("ClientA-FirstName");
        client.setLastName("ClientA-LastName");
        client.setPassword("$2a$10$hBn5gu6cGelJNiE6DDsaBOmZgyumCSzVwrOK/37FWgJ6aLIdZSSI2");
        client.setIsEmailVerified(true);
        clientRepository.save(client);
    }

    @Test
    public void testVerificationTokenNotUsableForLogin() {
        //initClientA();   //adaugat init daca se ruleaza doar testul asta
        Client client = clientRepository.findByUsernameIgnoreCase("ClientA").get();
        String token = verificationTokenService.generateEmailVerificationJWT(client);
        // daca cineva foloseste token-ul de verificare din email ca sa se logheze in aplicatie
        Assertions.assertNull(verificationTokenService.getUsername(token), "Token-ul de verificare nu ar trebui sa contina username-ul.");
    }

    @Test
    public void testAuthenticationTokenReturnsUsername() {
        //initClientA();   //adaugat init daca se ruleaza doar testul asta
        Client client = clientRepository.findByUsernameIgnoreCase("ClientA").get();
        String token = verificationTokenService.generateJWT(client);
        Assertions.assertEquals(client.getUsername(), verificationTokenService.getUsername(token), "Token-ul pentru autentificare ar trebui sa contina username-ul.");
    }

    @Test
    public void testResetPassToken() {
        initClientA();
        Client client = clientRepository.findByUsernameIgnoreCase("ClientA").get();
        String token = verificationTokenService.generatePasswordResetJWT(client);
        Assertions.assertEquals(client.getEmail(), verificationTokenService.getResetPassEmail(token), "Token-ul pentru autentificare ar trebui sa contina email-ul.");
    }

}
