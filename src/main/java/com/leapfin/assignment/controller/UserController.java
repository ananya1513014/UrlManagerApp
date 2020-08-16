package com.leapfin.assignment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.leapfin.assignment.bo.Response;
import com.leapfin.assignment.bo.User;
import com.leapfin.assignment.exceptions.DuplicateUserException;
import com.leapfin.assignment.service.PortfolioService;
import com.leapfin.assignment.service.TokenService;
import com.leapfin.assignment.service.UserService;
import com.leapfin.assignment.util.Constants;
import com.leapfin.assignment.util.Security;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping(Constants.USER_ENDPOINT)
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final static String AES_SECRET = System.getenv("aesSecret");
    private final UserService userService;
    private final TokenService tokenService;
    private final PortfolioService portfolioService;

    @Autowired
    public UserController(UserService userService, TokenService tokenService, PortfolioService portfolioService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.portfolioService = portfolioService;
    }

    @PostMapping
    private ResponseEntity createUser(@RequestBody String param){
        logger.info("Signup", param);

        HashMap<String, Object> responseObjectMap = new HashMap<>();

        try{
            Map<String, String> userInfoMap = new ObjectMapper().readValue(param, Map.class);
            User user = new User(userInfoMap.get(Constants.EMAIL), Security.encrypt(userInfoMap.get(Constants.PASSWORD), AES_SECRET));
            user = userService.addUser(user);
            portfolioService.createPortfolio(user.getStaticUserId());
            responseObjectMap.put("username", user.getEmail());
            return ResponseEntity.ok(new Response("200", "Sign up success", responseObjectMap));
        } catch (DuplicateUserException exp){
            logger.info("Duplicate User Exception encountered for : ", param);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new Response("412", "User exists with the specified ID", responseObjectMap));
        } catch (JsonProcessingException e) {
            logger.info("Json Processing Exception encountered for : ", param);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("400", "Request format is wrong", responseObjectMap));
        }
    }

    @GetMapping("/signin")
    private ResponseEntity login(@RequestBody String param){
        logger.info(param, " Signin");
        HashMap<String, Object> responseObjectMap = new HashMap<>();
        try{
            Map<String, String> userInfoMap = new ObjectMapper().readValue(param, Map.class);
            userService.verifyCred(userInfoMap.get(Constants.EMAIL), userInfoMap.get(Constants.PASSWORD));
            responseObjectMap.put("token", tokenService.getToken(userInfoMap.get(Constants.EMAIL), userInfoMap.get(Constants.PASSWORD)));
            return ResponseEntity.ok(new Response("200", "Login Success", responseObjectMap));
        } catch (AssertionError e) {
            logger.info("Invalid Credentials");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("401", "Invalid credentials", responseObjectMap));
        } catch (JsonProcessingException e) {
            logger.info("Json Processing Exception encountered for : ", param);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("400", "Request format is wrong", responseObjectMap));
        }
    }
}