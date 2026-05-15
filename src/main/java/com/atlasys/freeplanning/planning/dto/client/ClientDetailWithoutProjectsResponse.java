package com.atlasys.freeplanning.planning.dto.client;

import com.atlasys.freeplanning.planning.model.Client;

import java.util.UUID;

public record ClientDetailWithoutProjectsResponse(
        UUID id,
        String name,
        String email,
        String phone
) {
    public ClientDetailWithoutProjectsResponse(Client client) {
        this (
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getPhone()
        );
    }
}
