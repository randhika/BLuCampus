package com.mycampus.rontikeky.myacademic.Response;

/**
 * Created by Anggit on 08/08/2017.
 */
public class LoginResponse {
    private String status;
    private String message;
    private String token;

    public LoginResponse() {

    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}
