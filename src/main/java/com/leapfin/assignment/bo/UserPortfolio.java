package com.leapfin.assignment.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class UserPortfolio {
    @Id
    Long userId;
    String urlIdCsv;

    public UserPortfolio(Long userId) {
        this.userId = userId;
        this.urlIdCsv = "";
    }
}