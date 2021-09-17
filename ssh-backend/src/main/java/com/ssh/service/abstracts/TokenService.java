package com.ssh.service.abstracts;

import io.jsonwebtoken.Claims;

public interface TokenService {
    String generateToken(String username);

    boolean tokenValidate(String token);

    String getUserFromToken(String token);

    boolean isExpired(String token);

    Claims getClaims(String token);


}
