package com.atlasys.freeplanning.planning.dto.kanban;

import com.atlasys.freeplanning.planning.model.KanbanTask;

import java.util.UUID;

public record KanbanTaskResponse(
        UUID id,
        String title,
        String description,
        Integer position
) {
    public KanbanTaskResponse(KanbanTask task) {
        this(task.getId(), task.getTitle(), task.getDescription(), task.getPosition());
    }
}
