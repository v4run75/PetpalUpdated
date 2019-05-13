package com.crossapps.petpal.Server.Response;

public class RegisterResponse {
    private String message;
    private boolean success;
    private LoginResponseData data;

    public LoginResponseData getData() {
        return this.data;
    }

    public void setData(LoginResponseData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }

}
