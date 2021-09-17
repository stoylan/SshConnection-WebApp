package com.ssh.service.concrets;

import com.ssh.converter.CommandToCommandRequest;
import com.ssh.core.utilities.bean.Command;
import com.ssh.core.utilities.bean.Credential;
import com.ssh.core.utilities.results.DataResult;
import com.ssh.core.utilities.results.SuccessDataResult;
import com.ssh.repository.CommandRepository;
import com.ssh.repository.CredentialRepository;
import com.ssh.service.abstracts.CommandService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommandServiceImpl implements CommandService {
    private final CommandRepository commandRepository;
    private final CredentialRepository credentialRepository;
    private final CommandToCommandRequest commandToCommandRequest;

    public CommandServiceImpl(CommandRepository commandRepository, CredentialRepository credentialRepository, CommandToCommandRequest commandToCommandRequest) {
        this.commandRepository = commandRepository;
        this.credentialRepository = credentialRepository;
        this.commandToCommandRequest = commandToCommandRequest;
    }

    @Override
    public DataResult<Command> setCommand(Command command) {
        Credential credential = credentialRepository.getById(command.getCredential().getId());
        credential.getCommand().add(command);
        credentialRepository.save(credential);
        command.setCredential(credential);
        commandRepository.save(command);
        return new SuccessDataResult<>(command, "Succesfully set command");
    }


    @Override
    public DataResult<List<Command>> getCommands(int credentialId, int pageNo, int pageSize) {
        List<Command> commands = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        commandRepository.findByCredential_Id(credentialId, pageable).get().iterator().forEachRemaining(commands::add);
        return new SuccessDataResult<>(commands);
    }

}
