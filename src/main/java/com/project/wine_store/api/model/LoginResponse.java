package com.project.wine_store.api.model;

public class LoginResponse {
    private String jwtToken;
    private boolean success;
    private String failureReason;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwt) {
        this.jwtToken = jwt;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
