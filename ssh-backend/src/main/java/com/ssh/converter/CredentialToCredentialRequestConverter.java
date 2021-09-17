package com.ssh.converter;

import com.ssh.core.utilities.bean.Credential;
import com.ssh.core.utilities.request.CredentialRequest;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CredentialToCredentialRequestConverter implements Converter<Credential, CredentialRequest> {
    @Synchronized
    @Nullable
    @Override
    public CredentialRequest convert(Credential credential) {
        if (credential == null)
            return null;
        final CredentialRequest credentialRequest = new CredentialRequest();
        credentialRequest.setCommandRequest(credential.getCommandRequest());
        credentialRequest.setId(credential.getId());
        credentialRequest.setConnection(credential.isConnection());
        credentialRequest.setHost(credential.getHost());
        credentialRequest.setPassword(credential.getPassword());
        credentialRequest.setPort(credential.getPort());
        credentialRequest.setUsername(credential.getUsername());
        credentialRequest.setSessionTimeout(credential.getSessionTimeout());
        credentialRequest.setChannelTimeout(credential.getChannelTimeout());
        return credentialRequest;

    }
}
