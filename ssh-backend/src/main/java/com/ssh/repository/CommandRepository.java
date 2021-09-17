package com.ssh.repository;

import com.ssh.core.utilities.bean.Command;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandRepository extends PagingAndSortingRepository<Command, Integer> {
    Optional<List<Command>> findByCredential_Id(int credentialId, Pageable pageable);
}
