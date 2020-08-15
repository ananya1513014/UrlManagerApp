package com.leapfin.assignment.repository;

import com.leapfin.assignment.bo.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, String> {
    User findByEmail(String email);
}