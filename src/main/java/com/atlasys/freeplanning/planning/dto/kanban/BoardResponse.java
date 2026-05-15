package com.atlasys.freeplanning.planning.dto.kanban;

import java.util.List;

public record BoardResponse(
    List<KanbanColumnResponse> columns
) { }
