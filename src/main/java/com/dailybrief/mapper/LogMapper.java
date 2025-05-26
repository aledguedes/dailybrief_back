package com.dailybrief.mapper;

import java.util.List;
import java.util.Map;
import java.util.Collections;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.dailybrief.dto.LogRequestDTO;
import com.dailybrief.dto.LogResponseDTO;
import com.dailybrief.model.Log;

@Mapper(componentModel = "spring")
public interface LogMapper {

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "reportId", source = "reportId")
    @Mapping(target = "level", source = "level")

    @Mapping(target = "details", source = "details", qualifiedByName = "mapDetailsMapToString")
    Log toEntity(LogRequestDTO request);

    @Mapping(source = "createdBy", target = "created_by")
    @Mapping(source = "reportId", target = "reportId")
    @Mapping(source = "level", target = "level")

    @Mapping(source = "details", target = "details", qualifiedByName = "mapDetailsStringToMap")
    LogResponseDTO toResponse(Log log);

    List<LogResponseDTO> toResponseList(List<Log> logs);

    @Named("mapDetailsMapToString")
    default String mapDetailsMapToString(Map<String, Object> details) {
        if (details == null || details.isEmpty()) {
            return "{}";
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(details);
        } catch (JsonProcessingException e) {

            System.err.println("Erro ao serializar detalhes do log de Map para JSON: " + e.getMessage());
            return "{}";
        }
    }

    @Named("mapDetailsStringToMap")
    default Map<String, Object> mapDetailsStringToMap(String details) {
        if (details == null || details.trim().isEmpty()) {
            return Collections.emptyMap();
        }
        try {

            return OBJECT_MAPPER.readValue(details,
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {
                    });
        } catch (JsonProcessingException e) {

            System.err.println("Erro ao desserializar detalhes do log de JSON para Map: " + e.getMessage());
            return Collections.singletonMap("error", "Failed to parse details JSON");
        }
    }
}