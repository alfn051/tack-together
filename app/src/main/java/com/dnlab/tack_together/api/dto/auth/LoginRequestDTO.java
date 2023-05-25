package com.dnlab.tack_together.api.dto.auth;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginRequestDTO implements Serializable {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}