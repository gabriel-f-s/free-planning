package com.atlasys.freeplanning.planning.dto.project;

import com.atlasys.freeplanning.planning.dto.client.ClientDetailWithoutProjectsResponse;
import com.atlasys.freeplanning.planning.model.Project;
import com.atlasys.freeplanning.planning.model.enums.Platform;
import com.atlasys.freeplanning.planning.model.enums.ProjectType;
import com.atlasys.freeplanning.planning.model.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ProjectDetailResponse(
        UUID id,
        String title,
        String description,
        Platform platform,
        Status status,
        ProjectType type,
        BigDecimal minimumValue,
        BigDecimal maximumValue,
        BigDecimal closedValue,
        LocalDate deliveryForecast,
        LocalDate deliveryDate,
        String annotation,
        Boolean isPersonalProject,
        ClientDetailWithoutProjectsResponse client
) {
    public ProjectDetailResponse(Project project) {
        this(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getPlatform(),
                project.getStatus(),
                project.getType(),
                project.getMinimumValue(),
                project.getMaximumValue(),
                project.getClosedValue(),
                project.getDeliveryForecast(),
                project.getDeliveryDate(),
                project.getAnnotation(),
                project.getIsPersonalProject(),
                new ClientDetailWithoutProjectsResponse(project.getClient())
        );
    }
}
