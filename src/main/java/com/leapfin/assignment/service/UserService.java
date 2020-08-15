package com.leapfin.assignment.service;

import com.leapfin.assignment.bo.User;
import com.leapfin.assignment.repository.UserRepo;
import com.leapfin.assignment.util.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static UserRepo userRepo;
    private final static String SECRET_KEY = "anan";
    private final static long ttlMillis = 1000000000;

    @Autowired
    public UserService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    public User addUser(User user){
        if(userRepo.findByEmail(user.getEmail()) != null)
            throw new NullPointerException(); //TODO
        return userRepo.save(user);
    }

    public boolean verifyCred(String email, String password) {
        System.out.println("Verifying creds");
        System.out.println(password);
        System.out.println("Foundz:" + userRepo.findByEmail(email).toString());
        return userRepo.findByEmail(email).getPassword().equals(Security.encrypt(password, "sec"));
    }

    public Long getStaticUserId(String userId) {
        return userRepo.findByEmail(userId).getStaticUserId();
    }
}
