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
        BigDecimal closedValue,
        LocalDate deliveryDate,
        Boolean isPersonalProject
) {
    public ProjectPipelineResponse(Project project) {
        this(
                project.getId(),
                project.getTitle(),
                project.getClosedValue(),
                project.getDeliveryDate(),
                project.getIsPersonalProject()
        );
    }
}
