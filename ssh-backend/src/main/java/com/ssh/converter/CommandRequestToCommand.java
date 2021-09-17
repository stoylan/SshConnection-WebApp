package com.ssh.converter;

import com.ssh.core.utilities.bean.Command;
import com.ssh.core.utilities.request.CommandRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandRequestToCommand implements Converter<CommandRequest, Command> {
    @Override
    public Command convert(CommandRequest commandRequest) {
        Command command = new Command();
        command.setId(commandRequest.getId());
        command.setResponse(commandRequest.getResponse());
        command.setCredential(commandRequest.getCredential());
        command.setCommand(commandRequest.getCommand());
        return command;
    }
}
