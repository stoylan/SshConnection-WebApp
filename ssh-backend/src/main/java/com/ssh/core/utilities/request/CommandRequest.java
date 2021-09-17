package com.ssh.core.utilities.request;

import com.ssh.core.utilities.bean.Credential;
import lombok.Data;

@Data
public class CommandRequest {
    private int id;

    private String command;

    private String response;

    private Credential credential;
}
