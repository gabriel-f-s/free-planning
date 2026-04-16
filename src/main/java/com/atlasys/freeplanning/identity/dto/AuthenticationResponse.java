package com.atlasys.freeplanning.identity.dto;

public record AuthenticationResponse(
        UserResponse user,
        String token
) {
}
