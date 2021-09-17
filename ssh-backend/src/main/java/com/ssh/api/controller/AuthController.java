package com.ssh.api.controller;

import com.ssh.core.utilities.request.LoginRequest;
import com.ssh.core.utilities.results.ErrorResult;
import com.ssh.core.utilities.results.Result;
import com.ssh.core.utilities.results.SuccessResult;
import com.ssh.service.abstracts.TokenService;
import com.ssh.service.concrets.FileTransferServiceWithJsch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AuthController {
    private static Logger logger = LoggerFactory.getLogger(FileTransferServiceWithJsch.class);
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Post requested login.");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            return ResponseEntity.ok(tokenService.generateToken(loginRequest.getUsername()));
        } catch (Exception e) {
            logger.error("Error of authentication.");
            //return ResponseEntity.ok("Username or password is invalid.");
            throw e;
        }
    }

    @GetMapping("/valid")
    public ResponseEntity<Result> tokenValidate(@RequestParam String token) {
        logger.info("Token validation started.");
        boolean isValid = tokenService.tokenValidate(token);
        logger.info("Token is valid : " + isValid);

        if (isValid)
            return ResponseEntity.ok(new SuccessResult());
        else return ResponseEntity.ok(new ErrorResult());
        //return ResponseEntity.ok(Boolean.toString(isValid));
    }

    @GetMapping("/isExpired")
    public ResponseEntity<Result> tokenIsExpired(@RequestParam String token) {
        try {
            if (token.equals("null"))
                return ResponseEntity.ok(new ErrorResult());
            if (tokenService.isExpired(token)) {
                return ResponseEntity.ok(new SuccessResult());
            } else return ResponseEntity.ok(new ErrorResult());
        } catch (Exception e) {
            return ResponseEntity.ok(new ErrorResult());
        }
    }

}