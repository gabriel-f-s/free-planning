package com.atlasys.freeplanning.identity.dto;

public record AuthenticationResponse(
        String token,
        UserResponse user
) {
}
