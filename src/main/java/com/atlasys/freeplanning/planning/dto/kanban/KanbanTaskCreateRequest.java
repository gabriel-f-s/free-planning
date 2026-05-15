package com.atlasys.freeplanning.planning.dto.kanban;

import java.util.UUID;

public record KanbanTaskCreateRequest(
        String title,
        String description,
        Integer position
) {
}
