package com.leapfin.assignment.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class UrlBo {
    @Id
    @GeneratedValue
    Long urlId;
    String url;
    boolean useCaches;
    boolean doInput;
    boolean doOutput;
    boolean allowUserIteraction;
    long expiration;
    String header;
    long lastModified;
    String content;
    String encoding;
    String contentType;
    int contentLength;

    public UrlBo(String url){
        this.url = url;
    }
}