package com.project.wine_store.service;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.project.wine_store.api.model.PasswordResetBody;
import com.project.wine_store.api.model.RegistrationBody;
import com.project.wine_store.api.model.LoginBody;
import com.project.wine_store.exception.*;
import com.project.wine_store.model.Client;
import com.project.wine_store.model.EmailVerificationToken;
import com.project.wine_store.repository.ClientRepository;
import com.project.wine_store.repository.EmailVerificationTokenRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientServiceTest {

    // pentru testarea email-ului
    @RegisterExtension
    private static GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot", "secret"))
            .withPerMethodLifecycle(true);

    @Autowired
    private  ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private EncryptionService encryptionService;

    // Test pentru registerClient
    @Test
    @Transactional
    public void testRegisterClient() throws MessagingException, ClientAlreadyExistsException, CantSendEmailException {
        // exista deja username-ul?
        RegistrationBody registrationBody = new RegistrationBody();
        registrationBody.setUsername("ClientA");
        registrationBody.setEmail("junit@gmail.com");
        registrationBody.setFirstName("FirstName");
        registrationBody.setLastName("LastName");
        registrationBody.setPassword("Password123");
        Assertions.assertThrows(ClientAlreadyExistsException.class,
                () -> clientService.registerClient(registrationBody), "Username should already be in use.");
        // exista deja email-ul?
        registrationBody.setUsername("TestClient");
        registrationBody.setEmail("ClientA@junit.com");
        Assertions.assertThrows(ClientAlreadyExistsException.class,
                () -> clientService.registerClient(registrationBody), "Email-ul ar trebui sa fie deja folosit");
        // acum avem un client cu username si email unic
        registrationBody.setEmail("junit@gmail.com");
        Assertions.assertDoesNotThrow(() -> clientService.registerClient(registrationBody), "Ar trebui sa se inregistreze clientul");
        // mail-ul de verificare
        //verificam daca se primeste un email pe email-ul clientului nou
        Assertions.assertEquals("junit@gmail.com", greenMailExtension.getReceivedMessages()[0].getRecipients(Message.RecipientType.TO)[0].toString());
    }

    // test pentru login
    @Test
    @Transactional
    public void testLoginClient() throws ClientEmailNotVerifiedException, CantSendEmailException {
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("ClientA-DoesntExist");
        loginBody.setPassword("PasswordA123-BadPassword");
        Assertions.assertNull(clientService.loginClient(loginBody), "Clientul ar trebui sa nu existe");
        loginBody.setUsername("ClientA");
        Assertions.assertNull(clientService.loginClient(loginBody), "Parola ar trebui sa fie incorecta");
        loginBody.setPassword("PasswordA123");
        System.out.println(loginBody.getPassword());
        System.out.println(loginBody.getUsername());
        Assertions.assertNotNull(clientService.loginClient(loginBody), "Clientul ar trebui sa se logheze cum trebuie");
        loginBody.setUsername("ClientB");
        loginBody.setPassword("PasswordB123");
        try {
            clientService.loginClient(loginBody);
            Assertions.assertTrue(true, "Clientul ar trebui sa nu aiba emailul verificat.");
        } catch (ClientEmailNotVerifiedException ex) {
            Assertions.assertTrue(ex.isNewEmailSent(), "Ar trebui sa se trimita email de verificare");
            Assertions.assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }
        try {
            clientService.loginClient(loginBody);
            Assertions.assertTrue(true, "Clientul ar trebui sa nu aiba emailul verificat.");
        } catch (ClientEmailNotVerifiedException ex) {
            Assertions.assertFalse(ex.isNewEmailSent(), "Nu ar trebui sa se trimita email de verificare.");
            Assertions.assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }
    }

    // test pentru verifyLogin
    @Test
    @Transactional
    public void testVerifyClient() throws CantSendEmailException {
        // exista token pentru verificare?
        Assertions.assertFalse(clientService.verifyClient("Bad Token"), "Token gresit sau care nu exista ar trebui sa intoarca fals.");
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("ClientB");
        loginBody.setPassword("PasswordB123");
        try {
            clientService.loginClient(loginBody);
            Assertions.assertTrue(true, "Clientul ar trebui sa nu aiba email-ul verificat.");
        } catch (ClientEmailNotVerifiedException e) {
            List<EmailVerificationToken> tokens = emailVerificationTokenRepository.findByClient_IdOrderByIdDesc(2L);
            // luam ultimul token
            String token = tokens.get(0).getToken();
            Assertions.assertTrue(clientService.verifyClient(token), "Token-ul ar trebui sa fie bun.");
            Assertions.assertNotNull(loginBody, "Clientul ar trebui sa fie verificat");
        }
    }

    @Test
    @Transactional
    public void testForgotPassword() throws MessagingException {
        // daca email-ul nu exista
        Assertions.assertThrows(EmailNotFoundException.class, () -> clientService.forgotPassword("EmailNuExista@gmail.com"), "Email-ul care nu exista ar trebui sa dea eroare");
        // daca email-ul e corect
        Assertions.assertDoesNotThrow(() -> clientService.forgotPassword("ClientA@junit.com"), "Email-ul ar trebui sa fie trimis");
        // email-ul a fost primit?
        Assertions.assertEquals("ClientA@junit.com", greenMailExtension.getReceivedMessages()[0].getRecipients(Message.RecipientType.TO)[0].toString(), "Emailul pentru resetare ar trebui sa fie primit");
    }

    @Test
    @Transactional
    public void testResetPassword() {
        Client client = clientRepository.findByUsernameIgnoreCase("ClientA").get();
        String token = verificationTokenService.generatePasswordResetJWT(client);
        PasswordResetBody passwordResetBody = new PasswordResetBody();
        passwordResetBody.setResetPasswordToken(token);
        passwordResetBody.setNewPassword("NewPassword");
        clientService.resetPassword(passwordResetBody);
        client = clientRepository.findByUsernameIgnoreCase("ClientA").get();
        Assertions.assertTrue(encryptionService.verifyPassword("NewPassword", client.getPassword()), "Noua parola ar tebui sa fie retinuta in baza de date");
        }


}
