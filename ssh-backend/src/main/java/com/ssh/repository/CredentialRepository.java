package com.ssh.repository;

import com.ssh.core.utilities.bean.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Integer> {
    Optional<Credential> findByHostAndUsernameAndPort(String host, String username, Integer port);
}
