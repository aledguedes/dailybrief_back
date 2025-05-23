package com.dailybrief.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dailybrief.dto.LogRequestDTO;
import com.dailybrief.dto.LogResponseDTO;
import com.dailybrief.model.Log;

@Mapper(componentModel = "spring")
public interface LogMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "timestamp", source = "timestamp")
    Log toEntity(LogRequestDTO request);

    @Mapping(source = "createdBy", target = "created_by")
    LogResponseDTO toResponse(Log log);

    List<LogResponseDTO> toResponseList(List<Log> logs);
}