package com.ssh.service.auth;

import com.ssh.service.abstracts.TokenService;
import com.ssh.service.concrets.FileTransferServiceWithJsch;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenManager implements TokenService {
    //5*60
    private static final int validatiy = 5 * 60 * 1000;
    private static Logger logger = LoggerFactory.getLogger(FileTransferServiceWithJsch.class);
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Override
    public String generateToken(String username) {
        logger.info("Token generated succesfuly.");
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("Admin")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validatiy))
                .signWith(key)
                .compact();
    }

    @Override
    public boolean tokenValidate(String token) {
        try {
            if (getUserFromToken(token) != null && isExpired(token)) {
                return true;
            }
            logger.error("Token is not valid. Please login.");
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public String getUserFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    @Override
    public boolean isExpired(String token) {
        Claims claims = getClaims(token);
        boolean isExpired = claims.getExpiration().after(new Date(System.currentTimeMillis()));
        if (isExpired)
            logger.info("Token is not expired.");
        else logger.error("Token expired please authorization again.");
        return isExpired;
    }

    @Override
    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
