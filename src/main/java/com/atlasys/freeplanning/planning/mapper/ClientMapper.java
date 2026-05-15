package com.atlasys.freeplanning.planning.mapper;

import com.atlasys.freeplanning.planning.dto.client.ClientRequest;
import com.atlasys.freeplanning.planning.model.Client;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ClientRequest dto, @MappingTarget Client entity);
}
