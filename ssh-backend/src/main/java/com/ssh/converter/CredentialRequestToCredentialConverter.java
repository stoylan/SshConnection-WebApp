package com.ssh.converter;

import com.ssh.core.utilities.bean.Credential;
import com.ssh.core.utilities.request.CredentialRequest;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CredentialRequestToCredentialConverter implements Converter<CredentialRequest, Credential> {
    @Synchronized
    @Nullable
    @Override
    public Credential convert(CredentialRequest credentialRequest) {
        if (credentialRequest == null)
            return null;
        final Credential credential = new Credential();
        credential.setCommandRequest(credentialRequest.getCommandRequest());
        credential.setId(credentialRequest.getId());
        credential.setConnection(credentialRequest.isConnection());
        credential.setHost(credentialRequest.getHost());
        credential.setPassword(credentialRequest.getPassword());
        credential.setPort(credentialRequest.getPort());
        credential.setUsername(credentialRequest.getUsername());
        credential.setSessionTimeout(credentialRequest.getSessionTimeout());
        credential.setChannelTimeout(credentialRequest.getChannelTimeout());
        return credential;
    }
}
