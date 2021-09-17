package com.ssh.service.abstracts;

import com.ssh.core.utilities.bean.Command;
import com.ssh.core.utilities.results.DataResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommandService {
    DataResult<Command> setCommand(Command command);

    DataResult<List<Command>> getCommands(int credentialId, int pageNo, int pageSize);
}
