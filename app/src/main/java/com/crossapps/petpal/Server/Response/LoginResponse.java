package com.crossapps.petpal.Server.Response;

public class LoginResponse {
    private LoginResponseData data;
    private boolean success;
    private String message;

    public LoginResponseData getData() {
        return this.data;
    }

    public void setData(LoginResponseData data) {
        this.data = data;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
