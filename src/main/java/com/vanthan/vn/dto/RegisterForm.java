package com.vanthan.vn.dto;


import lombok.Data;

@Data
public class RegisterForm {
    private String fullName;
    private String email;
    private String username;
    private String password;
}
