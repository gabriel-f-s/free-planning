package com.atlasys.freeplanning.identity.dto;

import com.atlasys.freeplanning.identity.model.enums.Occupation;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record RegisterRequest(
        String name,
        String email,
        @Size(min = 8, message = "The new password must be at least 8 characters long") String password,
        Occupation occupation,
        BigDecimal hourlyRate
) {
}
