package com.ssh.core.utilities.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;

}
