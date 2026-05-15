package com.atlasys.freeplanning.planning.mapper;

import com.atlasys.freeplanning.planning.dto.kanban.*;
import com.atlasys.freeplanning.planning.model.KanbanColumn;
import com.atlasys.freeplanning.planning.model.KanbanTask;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KanbanMapper {
    default BoardResponse toResponse(List<KanbanColumn> columns) {
        if (columns == null) {
            return null;
        }
        return new BoardResponse(toColumnResponseList(columns));
    }
    List<KanbanColumnResponse> toColumnResponseList(List<KanbanColumn> columns);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    KanbanColumnResponse toColumnResponse(KanbanColumn column);

    KanbanTaskResponse toTaskResponse(KanbanTask task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(KanbanColumnRenameRequest dto, @MappingTarget KanbanColumn entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(KanbanTaskUpdateRequest dto, @MappingTarget KanbanTask entity);
}
