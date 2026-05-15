package com.atlasys.freeplanning.identity.dto;

import com.atlasys.freeplanning.identity.model.enums.Occupation;

import java.math.BigDecimal;

public record UserUpdateRequest(
        String name,
        Occupation occupation,
        BigDecimal hourlyRate
) {
}
