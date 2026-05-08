package com.atlasys.freeplanning.planning.dto.dashboard;

import com.atlasys.freeplanning.planning.dto.project.ProjectPipelineResponse;

import java.util.List;

public record DashboardPipelineResponse(
        List<ProjectPipelineResponse> inProgressProjects,
        List<ProjectPipelineResponse> underNegotiationProjects,
        List<ProjectPipelineResponse> onHoldProjects
) {
    public DashboardPipelineResponse() {
        this(List.of(), List.of(), List.of());
    }
}
