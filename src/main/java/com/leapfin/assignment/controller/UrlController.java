package com.leapfin.assignment.controller;

import com.leapfin.assignment.bo.Response;
import com.leapfin.assignment.bo.UrlBo;
import com.leapfin.assignment.bo.UserPortfolio;
import com.leapfin.assignment.service.PortfolioService;
import com.leapfin.assignment.service.TokenService;
import com.leapfin.assignment.service.UrlService;
import com.leapfin.assignment.service.UserService;
import com.leapfin.assignment.util.Constants;
import com.leapfin.assignment.util.UrlProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(Constants.URL_ENDPOINT)
@RestController
public class UrlController {

    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);

    private final PortfolioService portfolioService;
    private final UrlService urlService;
    private final TokenService tokenService;
    private final UserService userService;

    public UrlController(UserService userService, PortfolioService portfolioService, UrlService urlService, TokenService tokenService){
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.urlService = urlService;
        this.tokenService = tokenService;
    }

    @PostMapping
    private ResponseEntity addUrl(@RequestBody String param) throws IOException {
        logger.info("Add URL", param);

        Map<String, String> paramMap = new ObjectMapper().readValue(param, Map.class);
        Response response = new Response();
        HashMap<String, Object> map = new HashMap<>();

        String token = paramMap.get(Constants.TOKEN);
        Long userId = userService.getStaticUserId(tokenService.getUserId(token));
        String url = paramMap.get(Constants.URL);

        if(tokenService.authorize(tokenService.getUserId(token), token)) {
            map.put("isAuthorized", true);
            UrlBo urlBo = UrlProcessor.processUrl(url);
            urlBo = urlService.addUrl(urlBo);
            map.put("url", urlBo);
            map.put("portfolio", portfolioService.addUrlToPortfolio(userId, urlBo.getUrlId().toString()));
            response.setResponseCode(Constants.SUCCESS_RESPONSE_CODE);
            response.setMessage(Constants.SUCCESS_MESSAGE);
            response.setObjectMap(map);
            return ResponseEntity.ok(response);
        } else{
            response.setResponseCode(Constants.FAILURE_RESPONSE_CODE);
            response.setMessage(Constants.FAILURE_MESSAGE);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }
    }

    @GetMapping
    private ResponseEntity getUrls(@RequestBody String param) throws JsonProcessingException {
        logger.info("Get URLs", param);

        Map<String, String> paramMap = new ObjectMapper().readValue(param, Map.class);
        Response response = new Response();
        HashMap<String, Object> map = new HashMap<>();

        String token = paramMap.get(Constants.TOKEN);
        Long userId = userService.getStaticUserId(tokenService.getUserId(token));

        if(tokenService.authorize(tokenService.getUserId(token), token)) {
            map.put("isAuthorized", true);

            UserPortfolio portfolio = portfolioService.getUserPortfolio(userId);
            List<UrlBo> urlBoList = new ArrayList<>();
            for(String urlId: portfolio.getUrlIdCsv().split(Constants.SEPARATOR)){
                if(urlId.length()==0) continue;
                urlBoList.add(urlService.getUrl(urlId));
            }

            map.put("URLs", urlBoList);

            response.setResponseCode(Constants.SUCCESS_RESPONSE_CODE);
            response.setMessage(Constants.SUCCESS_MESSAGE);
            response.setObjectMap(map);
            return ResponseEntity.ok(response);
        } else{
            response.setResponseCode(Constants.FAILURE_RESPONSE_CODE);
            response.setMessage(Constants.FAILURE_MESSAGE);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }
    }
}