package com.leapfin.assignment.repository;

import com.leapfin.assignment.bo.AuthToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepo extends CrudRepository<AuthToken, String> {
    AuthToken findByEmail(String email);
}