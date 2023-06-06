package com.project.wine_store.api.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.project.wine_store.api.model.RegistrationBody;
import com.project.wine_store.service.ClientService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    // Pentru POST-urile din "/register" (care trimit email-uri de verificare)
    @RegisterExtension
    private static GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot", "secret"))
            .withPerMethodLifecycle(true);

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ClientService clientService;

    @Test
    @Transactional
    public void testRegister() throws Exception {
        RegistrationBody registrationBody = new RegistrationBody();
        ObjectMapper mapper = new ObjectMapper();
        // ce se intampla daca username-ul este null
        registrationBody.setUsername(null);
        registrationBody.setEmail("junitTest@gmail.com");
        registrationBody.setFirstName("FirstName");
        registrationBody.setLastName("LastName");
        registrationBody.setPassword("Password");
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(registrationBody))).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        // ce se intampla daca email-ul e null
        registrationBody.setUsername("Username");
        registrationBody.setEmail(null);
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(registrationBody))).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        // cand parola este null
        registrationBody.setEmail("junitTest@gmail.com");
        registrationBody.setPassword(null);
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(registrationBody))).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        // cand prenumele este null
        registrationBody.setPassword("Password");
        registrationBody.setFirstName(null);
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(registrationBody))).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        registrationBody.setFirstName("FirstName");
        registrationBody.setLastName(null);
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(registrationBody))).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        registrationBody.setLastName("LastName");
        // Verificam pentru fiecare atribut daca este blank
        registrationBody.setUsername("");
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(registrationBody))).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        registrationBody.setUsername("Username");
        registrationBody.setEmail("");
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(registrationBody))).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        // cand parola este null
        registrationBody.setEmail("junitTest@gmail.com");
        registrationBody.setPassword("");
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(registrationBody))).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        // cand prenumele este null
        registrationBody.setPassword("Password");
        registrationBody.setFirstName("");
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(registrationBody))).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        // cand numele este null
        registrationBody.setFirstName("FirstName");
        registrationBody.setLastName("");
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(registrationBody))).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        registrationBody.setLastName("LastName");
        // Acum ar trebui sa avem un registrationBody valid deci statusul ar trebui sa fie OK
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(registrationBody))).andExpect(status().is(HttpStatus.OK.value()));
    }

}