package com.atlasys.freeplanning.planning.dto.project;

import com.atlasys.freeplanning.planning.model.Project;
import com.atlasys.freeplanning.planning.model.enums.Platform;
import com.atlasys.freeplanning.planning.model.enums.ProjectType;
import com.atlasys.freeplanning.planning.model.enums.Status;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ProjectSummaryResponse(
        UUID id,
        String title,
        String description,
        Platform platform,
        Status status,
        ProjectType type,
        BigDecimal closedValue,
        LocalDate deliveryForecast,
        Boolean isPersonalProject,
        @Nullable String client
) {
    public ProjectSummaryResponse(Project project) {
        this(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getPlatform(),
                project.getStatus(),
                project.getType(),
                project.getClosedValue(),
                project.getDeliveryForecast(),
                project.getIsPersonalProject(),
                project.getClient() != null ? project.getClient().getName() : null);
    }
}
