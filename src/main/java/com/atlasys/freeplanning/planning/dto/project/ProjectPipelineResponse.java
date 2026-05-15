package com.atlasys.freeplanning.planning.dto.project;

import com.atlasys.freeplanning.planning.model.Project;
import com.atlasys.freeplanning.planning.model.enums.Platform;
import com.atlasys.freeplanning.planning.model.enums.ProjectType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ProjectPipelineResponse(
        UUID id,
        String title,
        Platform platform,
        ProjectType type,
        BigDecimal closedValue,
        LocalDate deliveryDate,
        String client
) {
    public ProjectPipelineResponse(Project project) {
        this(
                project.getId(),
                project.getTitle(),
                project.getPlatform(),
                project.getType(),
                project.getClosedValue(),
                project.getDeliveryDate(),
                project.getClient().getName()
        );
    }
}
