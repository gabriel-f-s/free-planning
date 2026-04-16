package com.atlasys.freeplanning.identity.dto;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.identity.model.enums.Occupation;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        Occupation occupation
) {

    public UserResponse(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getOccupation());
    }
}
