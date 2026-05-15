package com.atlasys.freeplanning.planning.dto.kanban;

public record KanbanTaskUpdateRequest(
        String title,
        String description
) {
}
