package com.leapfin.assignment.service;

import com.leapfin.assignment.bo.AuthToken;
import com.leapfin.assignment.repository.TokenRepo;
import com.leapfin.assignment.util.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
public class TokenService {

    private static TokenRepo tokenRepo;
    private final static String SECRET_KEY = System.getenv(Constants.SECRET_PARAM_KEY);
    private final static long ttlMillis = 1000000000;

    @Autowired
    public TokenService(TokenRepo tokenRepo){
        this.tokenRepo = tokenRepo;
    }

    public AuthToken saveAuthToken(AuthToken authToken){
        return tokenRepo.save(authToken);
    }

    public String getToken(String email, String password){

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(email).setIssuedAt(now).setSubject("subkect").setIssuer("issuwr").signWith(signatureAlgorithm, signingKey);

        if(ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return saveAuthToken(new AuthToken(email, builder.compact())).getToken();
    }

    public boolean authorize(String email, String token) {
        return tokenRepo.findByEmail(email).getToken().equals(token);
    }

    public String getUserId(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        return claims.getId();
    }
}