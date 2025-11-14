package com.dailybrief.mapper;

import com.dailybrief.dto.MaterialSourceLogDTO;
import com.dailybrief.model.MaterialSource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaterialSourceMapper {

    @Mapping(target = "rawId", source = "rawMaterial.id")
    MaterialSourceLogDTO toDto(MaterialSource materialSource);
}