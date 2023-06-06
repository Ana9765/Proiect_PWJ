package com.project.wine_store.exception;

public class ClientEmailNotVerifiedException extends Exception{
    private boolean sentNewEmail;

    public ClientEmailNotVerifiedException(boolean sentNewEmail) {
        this.sentNewEmail = sentNewEmail;
    }

    public boolean isNewEmailSent() {
        return sentNewEmail;
    }

}
