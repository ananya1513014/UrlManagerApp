package com.leapfin.assignment.service;

import com.leapfin.assignment.bo.User;
import com.leapfin.assignment.exceptions.DuplicateUserException;
import com.leapfin.assignment.repository.UserRepo;
import com.leapfin.assignment.util.Constants;
import com.leapfin.assignment.util.Security;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static UserRepo userRepo;
    private final static String AES_SECRET = System.getenv("aesSecret");
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    public User addUser(User user) throws DuplicateUserException {
        logger.info("Adding new user");
        if(userRepo.findByEmail(user.getEmail()) != null)
            throw new DuplicateUserException(user.getEmail());
        return userRepo.save(user);
    }

    public void verifyCred(String email, String password) {
        logger.info("Verifying Credentials");
        Assert.assertEquals(userRepo.findByEmail(email).getPassword(), Security.encrypt(password, AES_SECRET));
        logger.info("Credentials verified successfully");
    }

    public Long getStaticUserId(String email) {
        logger.info("Searching for static userid for email", email);
        return userRepo.findByEmail(email).getStaticUserId();
    }
}