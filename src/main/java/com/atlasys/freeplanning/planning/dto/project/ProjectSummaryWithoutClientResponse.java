package com.atlasys.freeplanning.planning.dto.project;

import com.atlasys.freeplanning.planning.model.Project;
import com.atlasys.freeplanning.planning.model.enums.Platform;
import com.atlasys.freeplanning.planning.model.enums.ProjectType;
import com.atlasys.freeplanning.planning.model.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ProjectSummaryWithoutClientResponse(
        UUID id,
        String title,
        String description,
        Platform platform,
        Status status,
        ProjectType type,
        BigDecimal closedValue,
        LocalDate deliveryForecast
) {
    public ProjectSummaryWithoutClientResponse(Project project) {
        this(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getPlatform(),
                project.getStatus(),
                project.getType(),
                project.getClosedValue(),
                project.getDeliveryForecast()
        );
    }
}
