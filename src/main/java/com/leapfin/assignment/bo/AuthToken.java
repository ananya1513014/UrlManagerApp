package com.leapfin.assignment.bo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class AuthToken {
    @Id
    String email;
    String token;

    public AuthToken(String email, String token){
        this.email = email;
        this.token = token;
    }

    public AuthToken(){

    }
}