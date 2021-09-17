package com.ssh.core.utilities.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "Credential")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "command"})
public class Credential implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credential_id")
    private int id;

    @NotNull
    @Column(name = "host_name")
    private String host;

    @Nullable
    @Column(name = "port_address")
    private Integer port;

    @NotNull
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Nullable
    @Column(name = "session_timeout")
    private Integer sessionTimeout;

    @Nullable
    @Column(name = "channel_timeout")
    private Integer channelTimeout;

    @Column(name = "status")
    private boolean isConnection;

    @Nullable
    @Column(name = "command_request")
    private String commandRequest;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "credential")
    private List<Command> command;

}
