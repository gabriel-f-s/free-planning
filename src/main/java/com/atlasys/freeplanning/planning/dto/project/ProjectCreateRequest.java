package com.atlasys.freeplanning.planning.dto.project;

import com.atlasys.freeplanning.planning.model.enums.Platform;
import com.atlasys.freeplanning.planning.model.enums.ProjectType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ProjectCreateRequest(
        String title,
        String description,
        Platform platform,
        ProjectType type,
        BigDecimal minimumValue,
        BigDecimal maximumValue,
        BigDecimal closedValue,
        LocalDate deliveryForecast,
        LocalDate deliveryDate,
        UUID clientId
) {
}
