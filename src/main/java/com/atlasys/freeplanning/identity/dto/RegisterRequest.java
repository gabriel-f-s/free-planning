package com.atlasys.freeplanning.identity.dto;

import com.atlasys.freeplanning.identity.model.enums.Occupation;

import java.math.BigDecimal;

public record RegisterRequest(
        String name,
        String email,
        String password,
        Occupation occupation
) {
}
