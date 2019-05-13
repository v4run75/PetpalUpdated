package com.crossapps.petpal.Server.Response;

public class RegisterResponseData {
    private String userId;
    private String email;
    private String password;
    private String mobileNo;
    private String name;

    public String getMobileNo() {
        return mobileNo;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUser_id() {
        return userId;
    }
}
