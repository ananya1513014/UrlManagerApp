package com.leapfin.assignment.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long staticUserId;
    private String email;
    private String password;

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }
}