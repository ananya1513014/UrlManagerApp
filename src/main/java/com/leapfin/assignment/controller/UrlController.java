package com.leapfin.assignment.controller;

import com.leapfin.assignment.bo.Response;
import com.leapfin.assignment.bo.UrlBo;
import com.leapfin.assignment.bo.UserPortfolio;
import com.leapfin.assignment.service.PortfolioService;
import com.leapfin.assignment.service.TokenService;
import com.leapfin.assignment.service.UrlService;
import com.leapfin.assignment.service.UserService;
import com.leapfin.assignment.util.Constants;
import com.leapfin.assignment.util.FileUtil;
import com.leapfin.assignment.util.UrlProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
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
    private ResponseEntity addUrl(@RequestHeader("token") String token, @RequestBody String param) {

        logger.info("Add URL", param);
        HashMap<String, Object> responseObjectMap = new HashMap<>();

        try {
            Map<String, String> paramMap = new ObjectMapper().readValue(param, Map.class);

            Long userId = userService.getStaticUserId(tokenService.getUserId(token));
            String url = paramMap.get(Constants.URL);

            tokenService.authorize(tokenService.getUserId(token), token);
            logger.info("Request is authorized");

            UrlBo urlBo = urlService.addUrl(UrlProcessor.processUrl(url));
            logger.info("Url added to DB", urlBo);

            portfolioService.addUrlToPortfolio(userId, urlBo.getUrlId().toString());
            logger.info("Url mapped to user", userId);

            return ResponseEntity.ok(new Response("200", "Url added successfully", responseObjectMap));
        } catch (AssertionError e){
            logger.info("Invalid Token Received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("401", "Invalid Token", responseObjectMap));
        } catch (JsonProcessingException e){
            logger.info("Request format is not as expected", param);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("400", "Request format is wrong", responseObjectMap));
        } catch (IOException e){
            logger.info("Error in file IO encountered");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("417", "File IO error", responseObjectMap));
        }
    }

    @GetMapping
    private ResponseEntity getUrls(@RequestHeader("token") String token){
        logger.info("Get URLs");

        HashMap<String, Object> map = new HashMap<>();

        try {
            Long userId = userService.getStaticUserId(tokenService.getUserId(token));

            tokenService.authorize(tokenService.getUserId(token), token);
            logger.info("Authorization success");

            UserPortfolio portfolio = portfolioService.getUserPortfolio(userId);
            logger.info("Portfolio fetched");

            List<UrlBo> urlBoList = new ArrayList<>();
            for (String urlId : portfolio.getUrlIdCsv().split(Constants.SEPARATOR)) {
                if (urlId.length() == 0) continue;
                UrlBo url = urlService.getUrl(urlId);
                String content = FileUtil.readFile(url.getContent());
                url.setContent(content);
                urlBoList.add(url);
            }
            map.put("URLs", urlBoList);
            return ResponseEntity.ok(new Response("200", "Success", map));
        } catch (AssertionError e){
            logger.info("Invalid Token Received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("401", "Invalid Token", map));
        }  catch (IOException e){
            logger.info("Error in file IO encountered");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("417", "File IO error", map));
        }
    }
}