package com.atlasys.freeplanning.identity.dto;

public record UserUpdateEmailRequest(
        String email,
        String password
) {
}
