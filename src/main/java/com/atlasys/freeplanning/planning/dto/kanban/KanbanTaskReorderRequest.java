package com.atlasys.freeplanning.planning.dto.kanban;

import java.util.UUID;

public record KanbanTaskReorderRequest(
        UUID columnId,
        Integer position
) {
}
