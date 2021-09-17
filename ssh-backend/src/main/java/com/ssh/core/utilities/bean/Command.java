package com.ssh.core.utilities.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Command {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "command_id")
    private int id;

    @Column(name = "command")
    private String command;

    @Column(name = "response")
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String response;

    @ManyToOne
    private Credential credential;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "execued_date", updatable = false)
    @ApiModelProperty(hidden = true)
    private Date executedDate;
    //private String executedDate;

    @Column(name = "issuedBy", updatable = false)
    @ApiModelProperty(hidden = true)
    private String issuedBy;

    public Command(String command, Credential credential, String response, String issuedBy) {
        this.command = command;
        this.credential = credential;
        this.response = response;
        this.issuedBy = issuedBy;
    }

    public Command() {
    }

}
