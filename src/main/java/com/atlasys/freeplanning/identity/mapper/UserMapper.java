package com.atlasys.freeplanning.identity.mapper;

import com.atlasys.freeplanning.identity.dto.UserUpdateRequest;
import com.atlasys.freeplanning.identity.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserUpdateRequest dto, @MappingTarget User entity);
}
