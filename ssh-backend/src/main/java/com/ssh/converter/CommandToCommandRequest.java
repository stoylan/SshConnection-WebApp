package com.ssh.converter;

import com.ssh.core.utilities.bean.Command;
import com.ssh.core.utilities.request.CommandRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandToCommandRequest implements Converter<Command, CommandRequest> {
    @Override
    public CommandRequest convert(Command command) {
        CommandRequest commandRequest = new CommandRequest();
        commandRequest.setId(command.getId());
        commandRequest.setResponse(command.getResponse());
        commandRequest.setCredential(command.getCredential());
        commandRequest.setCommand(command.getCommand());
        return commandRequest;
    }
}
