package com.atlasys.freeplanning.planning.dto.client;

import com.atlasys.freeplanning.planning.model.Client;

import java.util.UUID;

public record ClientSummaryResponse(
        UUID id,
        String name
) {
    public ClientSummaryResponse(Client client) {
        this (
            client.getId(),
            client.getName()
        );
    }
}
