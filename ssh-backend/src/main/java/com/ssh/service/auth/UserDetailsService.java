package com.ssh.service.auth;

import com.ssh.service.concrets.FileTransferServiceWithJsch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private static Logger logger = LoggerFactory.getLogger(FileTransferServiceWithJsch.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private Map<String, String> users = new HashMap<>();

    public UserDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void init() {
        users.put("admin", bCryptPasswordEncoder.encode("admin"));
        users.put("admin2", bCryptPasswordEncoder.encode("admin2"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (users.containsKey(username)) {
            logger.info("Entered right username and password.");
            return new User(username, users.get(username), new ArrayList<>());
        }
        throw new UsernameNotFoundException(username);
    }
}
