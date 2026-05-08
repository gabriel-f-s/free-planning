package com.atlasys.freeplanning.planning.dto.kanban;

import com.atlasys.freeplanning.planning.model.KanbanColumn;

import java.util.List;
import java.util.UUID;

public record KanbanColumnResponse(
        UUID id,
        String name,
        Integer position,
        List<KanbanTaskResponse> tasks
) {
    public KanbanColumnResponse(KanbanColumn column) {
        this(
                column.getId(),
                column.getName(),
                column.getPosition(),
                column.getTasks().stream().map(KanbanTaskResponse::new).toList()
        );
    }
}
