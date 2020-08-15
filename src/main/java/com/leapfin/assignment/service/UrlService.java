package com.leapfin.assignment.service;

import com.leapfin.assignment.bo.UrlBo;
import com.leapfin.assignment.repository.UrlRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    private static UrlRepo urlRepo;

    @Autowired
    public  UrlService(UrlRepo urlRepo){
        this.urlRepo = urlRepo;
    }

    public UrlBo addUrl(UrlBo url){
        return urlRepo.save(url);
    }

    public UrlBo getUrl(String urlId){
        return urlRepo.findByUrlId(Long.parseLong(urlId));
    }
}
