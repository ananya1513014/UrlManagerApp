package com.leapfin.assignment.service;

import com.leapfin.assignment.bo.UserPortfolio;
import com.leapfin.assignment.repository.PortfolioRepo;
import com.leapfin.assignment.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {

    private static PortfolioRepo portfolioRepo;

    @Autowired
    public PortfolioService(PortfolioRepo portfolioRepo){
        this.portfolioRepo = portfolioRepo;
    }

    public UserPortfolio createPortfolio(Long userId) {
        return portfolioRepo.save(new UserPortfolio(userId));
    }

    public UserPortfolio addUrlToPortfolio(Long userId, String urlId) {
        UserPortfolio portfolio = portfolioRepo.findByUserId(userId);
        portfolio.setUrlIdCsv(portfolio.getUrlIdCsv().concat(Constants.SEPARATOR).concat(urlId));
        return portfolioRepo.save(portfolio);
    }

    public UserPortfolio getUserPortfolio(Long userId){
           return portfolioRepo.findByUserId(userId);
    }
}