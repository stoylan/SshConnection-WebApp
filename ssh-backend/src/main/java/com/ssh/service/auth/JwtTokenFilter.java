package com.ssh.service.auth;

import com.ssh.service.abstracts.TokenService;
import com.ssh.service.concrets.FileTransferServiceWithJsch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private static Logger logger = LoggerFactory.getLogger(FileTransferServiceWithJsch.class);

    private final TokenService tokenService;

    public JwtTokenFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = httpServletRequest.getHeader("Authorization");
        String username = null;
        String token = null;
        if (authHeader != null && authHeader.contains("Bearer")) {
            token = authHeader.split(" ")[1].trim();
            try {
                logger.debug("Username getting from token");
                username = tokenService.getUserFromToken(token);
            } catch (Exception e) {
                logger.error("Invalid Token");
            }
        }

        if (username != null && token != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (tokenService.tokenValidate(token)) {
                logger.info("Authentication provided.");
                UsernamePasswordAuthenticationToken upassToken =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                upassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(upassToken);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}