package com.dailybrief.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.dailybrief.dto.LogRequestDTO;
import com.dailybrief.dto.LogResponseDTO;
import com.dailybrief.model.Log;

@Mapper(componentModel = "spring")
public interface LogMapper {
    Log toEntity(LogRequestDTO request);

    LogResponseDTO toResponse(Log log);

    List<LogResponseDTO> toResponseList(List<Log> logs);
}