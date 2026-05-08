package com.atlasys.freeplanning.planning.dto.client;

import com.atlasys.freeplanning.planning.model.Client;

public record ClientRequest(
        String name,
        String email,
        String phone
) {
    public ClientRequest(Client client) {
        this(
                client.getName(),
                client.getEmail(),
                client.getPhone()
        );
    }
}
