package com.project.wine_store.service;

import com.project.wine_store.exception.CantSendEmailException;
import com.project.wine_store.model.Client;
import com.project.wine_store.model.EmailVerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    @Value("${from.email}")
    private String fromEmail;
    @Value("${store.url}")
    private String storeURL;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private SimpleMailMessage generateMailMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        return simpleMailMessage;
    }

    // trimitem email-ul de verificare al contului
    public void sendVerificationEmail(EmailVerificationToken emailVerificationToken) throws CantSendEmailException {
        SimpleMailMessage simpleMailMessage = generateMailMessage();
        // luam email-ul clientului
        simpleMailMessage.setTo(emailVerificationToken.getClient().getEmail());
        simpleMailMessage.setSubject("Email de activare al contului");
        simpleMailMessage.setText("Pentru a-ti activa contul de pe WineStore, acceseaza link-ul urmator:\n" + storeURL + "/auth/verify?token=" + emailVerificationToken.getToken()); //try MIME MESSAGES IF YOU HAVE TIME
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new CantSendEmailException();
        }
    }

    // trimitem email-ul de resetare al parolei
    public void sendResetPassEmail (Client client, String resetPassToken) throws CantSendEmailException {
        SimpleMailMessage simpleMailMessage = generateMailMessage();
        simpleMailMessage.setTo(client.getEmail());
        simpleMailMessage.setSubject("Email pentru resetarea parolei");
        simpleMailMessage.setText("Pentru a-ti reseta parola de pe WineStore, acceseaza link-ul urmator: \n" + storeURL + "/auth/resetPassword?token=" + resetPassToken);
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new CantSendEmailException();
        }
    }

}
