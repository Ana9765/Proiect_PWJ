package com.project.wine_store.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class EncryptionServiceTest {

    @Autowired
    private EncryptionService encryptionService;

    @Test
    public void testPasswordEncryption() {
        String password = "ParolaMea*123";
        String hash = encryptionService.encryptPassword(password);
        Assertions.assertTrue(encryptionService.verifyPassword(password, hash), "Parola hash ar trebui sa fie ca originala");
        Assertions.assertFalse(encryptionService.verifyPassword(password + "inPlus", hash), "Parola noua nu ar trebui sa fie corecta");

    }
}
