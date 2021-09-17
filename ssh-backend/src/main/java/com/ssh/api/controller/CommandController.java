package com.ssh.api.controller;

import com.ssh.core.utilities.results.DataResult;
import com.ssh.service.abstracts.CommandService;
import com.ssh.service.concrets.FileTransferServiceWithJsch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@CrossOrigin
public class CommandController {
    private static Logger logger = LoggerFactory.getLogger(FileTransferServiceWithJsch.class);

    private final CommandService commandService;

    public CommandController(CommandService commandService) {
        this.commandService = commandService;
    }

    @GetMapping("command/getCommandByCredentialId")
    public ResponseEntity<DataResult> getCommands(int credentialId, int pageNo, int pageSize) {
        return ResponseEntity.ok(commandService.getCommands(credentialId, pageNo, pageSize));
    }


}
