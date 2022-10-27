package com.vanthan.vn.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "vn_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String fullName;
    private String email;
    private String username;
    private String password;
    private Timestamp createdAt;
}
