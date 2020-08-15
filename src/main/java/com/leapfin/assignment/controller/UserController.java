package com.leapfin.assignment.controller;

import com.leapfin.assignment.bo.Response;
import com.leapfin.assignment.bo.User;
import com.leapfin.assignment.service.PortfolioService;
import com.leapfin.assignment.service.TokenService;
import com.leapfin.assignment.service.UserService;
import com.leapfin.assignment.util.Constants;
import com.leapfin.assignment.util.Security;
import com.fasterxml.jackson.core.JsonProcessingException;
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
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final static String AES_SECRET = System.getenv(Constants.AES_SECRET_PARAM_KEY);
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
    private ResponseEntity createUser(@RequestBody String param) throws JsonProcessingException {
        logger.info("Signup", param);
        Map<String, String> userInfoMap = new ObjectMapper().readValue(param, Map.class);
        Response response = new Response();
        HashMap<String, Object> map = new HashMap<>();
        try{
            User user = new User(userInfoMap.get(Constants.EMAIL), Security.encrypt(userInfoMap.get(Constants.PASSWORD), AES_SECRET));
            user = userService.addUser(user);
            portfolioService.createPortfolio(user.getStaticUserId());
            map.put("username", user.getEmail());
            response.setResponseCode(Constants.SUCCESS_RESPONSE_CODE);
            response.setMessage(Constants.SUCCESS_MESSAGE);
            response.setObjectMap(map);
            return ResponseEntity.ok(response);
        } catch (Exception exp){
            response.setResponseCode(Constants.FAILURE_RESPONSE_CODE);
            response.setMessage(Constants.FAILURE_MESSAGE);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }
    }

    @GetMapping("/signin")
    private ResponseEntity login(@RequestBody String param) throws JsonProcessingException {
        logger.info(param, " Signin");
        Map<String, String> userInfoMap = new ObjectMapper().readValue(param, Map.class);
        Response response = new Response();
        HashMap<String, Object> map = new HashMap<>();
        try{
            if(userService.verifyCred(userInfoMap.get(Constants.EMAIL), userInfoMap.get(Constants.PASSWORD)))
            {
                String token = tokenService.getToken(userInfoMap.get(Constants.EMAIL), userInfoMap.get(Constants.PASSWORD));
                map.put("token", token);
                response.setResponseCode(Constants.SUCCESS_RESPONSE_CODE);
                response.setMessage(Constants.SUCCESS_MESSAGE);
                response.setObjectMap(map);
                return ResponseEntity.ok(response);
            }
            else{
                response.setResponseCode(Constants.INVALID_CREDS_RESPONSE_CODE);
                response.setMessage(Constants.INVALID_CREDS_MESSAGE);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception exp){
            response.setResponseCode(Constants.INVALID_CREDS_RESPONSE_CODE);
            response.setMessage(Constants.INVALID_CREDS_MESSAGE);
            map.put("Exception", exp);
            response.setObjectMap(map);
            return  ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }
    }
}