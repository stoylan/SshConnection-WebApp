package com.ssh.api.controller;

import com.ssh.core.utilities.request.CredentialRequest;
import com.ssh.core.utilities.results.DataResult;
import com.ssh.core.utilities.results.Result;
import com.ssh.service.abstracts.FileTransferService;
import com.ssh.service.concrets.FileTransferServiceWithJsch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/")
@CrossOrigin
public class SshController {
    private static Logger logger = LoggerFactory.getLogger(FileTransferServiceWithJsch.class);

    private final FileTransferService fileTransferService;

    public SshController(FileTransferService fileTransferService, AuthenticationManager authenticationManager) {
        this.fileTransferService = fileTransferService;
    }

    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public String getName(Authentication authentication, Principal principal) {
        System.out.println(authentication.getName());
        System.out.println("-----------------");
        System.out.println(principal.getName());
        return "";
    }

    @PostMapping("/ssh")
    public ResponseEntity<DataResult> executeCommand(@Valid @RequestBody CredentialRequest credential) throws Exception {
        logger.info("Execute command requested.");
        List<String> list = new ArrayList<String>(Arrays.asList(credential.getCommandRequest().split(";")));
        credential.setCommand(list);
        return ResponseEntity.ok(fileTransferService.listFolderStructure(credential));
    }

    @GetMapping("ssh/isConnect")
    public ResponseEntity<Result> isConnected(int credentialId) throws Exception {
        return ResponseEntity.ok(fileTransferService.getConnectStatus(credentialId));
    }

    @GetMapping("ssh/getEthernet")
    public ResponseEntity<DataResult> getEthernet(int credentalId) throws Exception {
        return ResponseEntity.ok(fileTransferService.getEthAddress(credentalId));
    }

    @GetMapping("ssh/getLocal")
    public ResponseEntity<DataResult> getLocal(int credentialId) throws Exception {
        return ResponseEntity.ok(fileTransferService.getLocalAddress(credentialId));
    }

    @PostMapping("ssh/add")
    public ResponseEntity<Result> addSsh(@Valid @RequestBody CredentialRequest credentialRequest) {
        return ResponseEntity.ok(fileTransferService.addShhServer(credentialRequest));
    }

    @GetMapping("ssh/getSsh")
    public ResponseEntity<DataResult> getSsh() {
        return ResponseEntity.ok(fileTransferService.getSsh());
    }

    @GetMapping("ssh/getSshById")
    public ResponseEntity<DataResult> getSshById(@RequestParam @Valid int id) {
        return ResponseEntity.ok(fileTransferService.getCredentialById(id));
    }

    @PostMapping("ssh/delete")
    public ResponseEntity<Result> deleteSshById(@RequestParam @Valid int id) {
        return ResponseEntity.ok(fileTransferService.deleteCredentialById(id));
    }

    @PostMapping("ssh/setCommand")
    public ResponseEntity<Result> setCommand(@RequestParam @Valid int id, String command) throws Exception {
        return ResponseEntity.ok(fileTransferService.setCommand(id, command));
    }

    @PostMapping("ssh/setStatus")
    public ResponseEntity<Result> setStatus() throws Exception {
        return ResponseEntity.ok(fileTransferService.setStatus());
    }

}
