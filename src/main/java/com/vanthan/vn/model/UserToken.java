package com.vanthan.vn.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "vn_user_token")
@Data

public class UserToken {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String token;
    private int userId;
}
