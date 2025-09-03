package com.dailybrief.mapper;

import com.dailybrief.dto.MaterialProcessInitialDTO;
import com.dailybrief.dto.MaterialProcessResponseDTO;
import com.dailybrief.dto.PythonMaterialResponseDTO;
import com.dailybrief.model.MaterialProcess;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class MaterialProcessMapper {

    @Autowired
    protected ObjectMapper objectMapper;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "rawMaterial", ignore = true)
    @Mapping(target = "sourceUrlsJson", ignore = true)
    @Mapping(target = "generatedContentJson", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "suggestedImagePrompt", ignore = true)
    public abstract MaterialProcess toEntity(MaterialProcessInitialDTO dto);

    @Mapping(target = "sourceUrls", source = "sourceUrlsJson", qualifiedByName = "jsonToList")
    @Mapping(target = "rawMaterial", source = "rawMaterial")
    @Mapping(target = "generatedContent", source = "generatedContentJson", qualifiedByName = "jsonToMap")
    public abstract MaterialProcessResponseDTO toResponseDto(MaterialProcess entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", source = "user_id")
    @Mapping(target = "taskId", source = "task_id")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
    @Mapping(target = "updatedAt", source = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
    @Mapping(target = "rawMaterial", source = "rawMaterial", qualifiedByName = "jsonNodeToString")
    @Mapping(target = "sourceUrlsJson", source = "sourceUrls", qualifiedByName = "listToJson")
    @Mapping(target = "generatedContentJson", source = "generatedContent", qualifiedByName = "mapToJson")
    @Mapping(target = "suggestedImagePrompt", source = "suggestedImagePrompt")
    public abstract void updateEntityFromPythonDto(PythonMaterialResponseDTO dto,
            @MappingTarget MaterialProcess entity);

    @Named("jsonToList")
    public List<String> jsonToList(String json) {
        if (json == null || json.isEmpty() || json.equals("null")) {
            return null;
        }
        try {
            return Arrays.asList(objectMapper.readValue(json, String[].class));
        } catch (JsonProcessingException e) {

            return null;
        }
    }

    @Named("listToJson")
    public String listToJson(List<String> list) {
        if (list == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {

            return null;
        }
    }

    @Named("jsonToMap")
    public Map<String, Object> jsonToMap(String json) {
        if (json == null || json.isEmpty() || json.equals("null")) {
            return null;
        }
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {

            return null;
        }
    }

    @Named("mapToJson")
    public String mapToJson(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {

            return null;
        }
    }

    @Named("jsonNodeToString")
    public String jsonNodeToString(JsonNode jsonNode) {
        if (jsonNode == null) {
            return null;
        }
        if (jsonNode.isTextual()) {
            return jsonNode.asText();
        }
        return jsonNode.toString();
    }
}
