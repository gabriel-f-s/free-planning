package com.atlasys.freeplanning.planning.dto.kanban;

public record KanbanTaskRequest(
        String title,
        String description,
        Integer position,
        Long columnId
) {
}
