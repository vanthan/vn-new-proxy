package com.vanthan.vn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResult {
    private Integer userId;
    private String userName;
    private String email;

    private String fullName;

    private String password;

    public UserResult(){}

    public UserResult(Integer userId, String userName, String email) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
    }
}
