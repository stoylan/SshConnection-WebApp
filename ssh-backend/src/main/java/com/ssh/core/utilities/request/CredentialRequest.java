package com.ssh.core.utilities.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
public class CredentialRequest {
    private int id;

    @NotNull
    private String host;

    @Nullable
    private Integer port;

    @NotNull
    private String username;

    private String password;

    @Nullable
    private Integer sessionTimeout;

    @Nullable
    private Integer channelTimeout;

    @JsonIgnore
    private List<String> command = new ArrayList<>();

    @Nullable
    private String commandRequest;

    @NotNull
    @Value("false")
    private boolean isConnection;


}
