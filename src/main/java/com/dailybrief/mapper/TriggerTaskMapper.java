package com.dailybrief.mapper;

import com.dailybrief.dto.TriggerTaskResponseDTO;
import com.dailybrief.model.TriggerTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TriggerTaskMapper {

    TriggerTaskMapper INSTANCE = Mappers.getMapper(TriggerTaskMapper.class);

    @Mapping(target = "triggerId", source = "triggerId")
    @Mapping(target = "taskId", source = "taskId")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "status", source = "status")
    TriggerTaskResponseDTO toDto(TriggerTask entity);

    List<TriggerTaskResponseDTO> toDtoList(List<TriggerTask> entities);
}