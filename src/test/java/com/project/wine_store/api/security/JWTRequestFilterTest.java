package com.project.wine_store.api.security;

import com.project.wine_store.model.Client;
import com.project.wine_store.repository.ClientRepository;
import com.project.wine_store.service.VerificationTokenService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JWTRequestFilterTest {


    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private MockMvc mvc;
    private static final String authorisationPath = "/auth/me";

    @Before
    public void initClientB() {
        Client client = new Client();
        client.setUsername("ClientB");
        client.setEmail("ClientB@junit.com");
        client.setFirstName("ClientB-FirstName");
        client.setLastName("ClientB-LastName");
        client.setPassword("$2a$10$TlYbg57fqOy/1LJjispkjuSIvFJXbh3fy0J9fvHnCpuntZOITAjVG");
        client.setIsEmailVerified(false);
        clientRepository.save(client);
    }

    // ce se intampla cand accesezi "/auth/me" fara un token
    @Test
    public void testNoTokenRequest() throws Exception {
        mvc.perform(get(authorisationPath)).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    // ce se intampla cand primim un token gresit dupa decode
    @Test
    public void testBadToken() throws Exception {
        mvc.perform(get(authorisationPath).header("Authorization", "InvalidBadToken")).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
        mvc.perform(get(authorisationPath).header("Authorization", "Bearer InvalidBadToken")).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    // daca email-ul nu este verificat dar primeste token
    @Test
    public void testEmailNotVerified() throws Exception {
        initClientB();
        Client client = new Client();
        client = clientRepository.findByUsernameIgnoreCase("ClientB").get();
        String token = verificationTokenService.generateJWT(client);
        mvc.perform(get(authorisationPath).header("Authorization", "Bearer " + token)).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    // daca totul merge ok
    @Test
    public void testSuccess() throws Exception {
        Client client = clientRepository.findByUsernameIgnoreCase("ClientA").get();
        String token = verificationTokenService.generateJWT(client);
        mvc.perform(get(authorisationPath).header("Authorization", "Bearer " + token)).andExpect(status().is(HttpStatus.OK.value()));
    }

}
