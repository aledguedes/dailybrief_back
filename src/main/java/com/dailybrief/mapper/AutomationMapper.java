package com.dailybrief.mapper;

import com.dailybrief.dto.AutomationDTO;
import com.dailybrief.model.Automation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutomationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Automation toEntity(AutomationDTO dto);
}
