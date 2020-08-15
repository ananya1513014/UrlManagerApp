package com.leapfin.assignment.repository;

import com.leapfin.assignment.bo.UrlBo;
import org.springframework.data.repository.CrudRepository;

public interface UrlRepo extends CrudRepository<UrlBo, Long> {
    UrlBo findByUrlId(Long urlId);
}