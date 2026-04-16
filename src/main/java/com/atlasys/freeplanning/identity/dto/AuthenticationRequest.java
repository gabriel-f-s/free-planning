package com.atlasys.freeplanning.identity.dto;

public record AuthenticationRequest(
        String email,
        String password
) {
}
