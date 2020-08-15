package com.leapfin.assignment.repository;

import com.leapfin.assignment.bo.UserPortfolio;
import org.springframework.data.repository.CrudRepository;

public interface PortfolioRepo extends CrudRepository<UserPortfolio, Long> {
    UserPortfolio findByUserId(Long userId);
}
