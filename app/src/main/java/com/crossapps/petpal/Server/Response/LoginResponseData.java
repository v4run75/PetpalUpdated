package com.crossapps.petpal.Server.Response;

public class LoginResponseData {
    private String userId;
    private String name;
    private String mobileNumber;
    private String email;
    private String profile_picture;


/*
{
    "success": true,
    "message": "Verified",
    "data": {
        "userId": "11",
        "aboutUser": "Bio",
        "email": "varun.webpulse@gmail.com",
        "firstName": "",
        "lastName": "",
        "gender": "M",
        "mobileNumber": "7838342613",
        "status": "1",
        "isVerified": "1",
        "city": "Delhi",
        "latitude": "28.67 N",
        "longitude": "77.21 E",
        "token": "get_auth_token"
    }
}
 */


    public String getName() {
        return name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getUserId() {
        return userId;
    }

}
