package com.atlasys.freeplanning.planning.repository;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    boolean existsClientByEmailAndOwner(String email, User owner);

    Page<Client> findAllByOwner(User loggedUser, Pageable pageable);

    Page<Client> findByOwnerAndNameContainingIgnoreCase(User loggedUser, String name, Pageable pageable );

    Optional<Client> findByOwnerAndId(User loggedUser, UUID id);
}
