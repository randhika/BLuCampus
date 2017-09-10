package com.mycampus.rontikeky.myacademic.Request;

/**
 * Created by Anggit on 08/08/2017.
 */
public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
