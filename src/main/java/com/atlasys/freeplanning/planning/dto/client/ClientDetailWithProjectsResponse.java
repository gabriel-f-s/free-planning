package com.atlasys.freeplanning.planning.dto.client;

import com.atlasys.freeplanning.planning.dto.project.ProjectSummaryWithoutClientResponse;
import com.atlasys.freeplanning.planning.model.Client;

import java.util.List;
import java.util.UUID;

public record ClientDetailWithProjectsResponse(
        UUID id,
        String name,
        String email,
        String phone,
        List<ProjectSummaryWithoutClientResponse> projects
) {
    public ClientDetailWithProjectsResponse(Client client) {
        List<ProjectSummaryWithoutClientResponse> projects = client.getProjects()
                .stream()
                .map(ProjectSummaryWithoutClientResponse::new)
                .toList();
        this (
            client.getId(),
            client.getName(),
            client.getEmail(),
            client.getPhone(),
            projects
        );
    }
}
