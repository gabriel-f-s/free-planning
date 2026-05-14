package com.atlasys.freeplanning.planning.dto.kanban;

public record KanbanColumnCreateRequest(
        String name,
        Integer position
) {
}
