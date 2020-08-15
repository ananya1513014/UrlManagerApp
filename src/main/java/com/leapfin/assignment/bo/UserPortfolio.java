package com.leapfin.assignment.bo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class UserPortfolio {
    @Id
    Long userId;
    String urlIdCsv;

    protected UserPortfolio(){

    }

    public UserPortfolio(Long userId) {
        this.userId = userId;
        this.urlIdCsv = "";
    }
}