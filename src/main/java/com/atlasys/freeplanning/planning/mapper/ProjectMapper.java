package com.atlasys.freeplanning.planning.mapper;

import com.atlasys.freeplanning.planning.dto.project.ProjectDetailResponse;
import com.atlasys.freeplanning.planning.dto.project.ProjectSummaryResponse;
import com.atlasys.freeplanning.planning.dto.project.ProjectUpdateRequest;
import com.atlasys.freeplanning.planning.model.Client;
import com.atlasys.freeplanning.planning.model.Project;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "client", ignore = true)
    void updateEntityFromDto(ProjectUpdateRequest dto, @MappingTarget Project entity);

    ProjectDetailResponse toDetailResponse(Project project);
    ProjectSummaryResponse toSummaryResponse(Project project);

    String getClientName(Client client);
}
