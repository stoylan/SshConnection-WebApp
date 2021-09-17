package com.ssh.service.abstracts;

import com.ssh.core.utilities.bean.Credential;
import com.ssh.core.utilities.request.CredentialRequest;
import com.ssh.core.utilities.results.DataResult;
import com.ssh.core.utilities.results.Result;

import java.util.List;

public interface FileTransferService {
    DataResult<String> listFolderStructure(CredentialRequest credential) throws Exception;

    void connect(CredentialRequest credentialRequest) throws Exception;

    void disconnect();

    void executeCommand(CredentialRequest credentialRequest) throws Exception;

    DataResult<Boolean> checkIsConnected(CredentialRequest credential) throws Exception;

    DataResult<Boolean> getConnectStatus(int credentialId) throws Exception;

    void calculateEthAddress(CredentialRequest credential) throws Exception;

    DataResult<String> getEthAddress(int credentialId) throws Exception;

    void calculateLocalAddress(CredentialRequest credentialRequest) throws Exception;

    DataResult<String> getLocalAddress(int credentialId) throws Exception;

    Result addShhServer(CredentialRequest credentialRequest);

    DataResult<List<Credential>> getSsh();

    DataResult<Credential> getCredentialById(int id);

    Result deleteCredentialById(int id);

    DataResult<CredentialRequest> setCommand(int credentialId, String command);

    Result setStatus() throws Exception;
}
