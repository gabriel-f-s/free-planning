package com.atlasys.freeplanning.planning.mapper;

import com.atlasys.freeplanning.planning.dto.project.ProjectUpdateRequest;
import com.atlasys.freeplanning.planning.model.Client;
import com.atlasys.freeplanning.planning.model.Project;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "client", ignore = true)
    void updateEntityFromDto(ProjectUpdateRequest dto, @MappingTarget Project entity);

}
