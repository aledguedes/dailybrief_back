package com.dailybrief.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.io.IOException;

public record PythonMaterialResponseDTO(
        String task_id,
        String user_id,
        String theme,
        @JsonProperty("raw_material") JsonNode rawMaterial,
        @JsonProperty("source_urls") List<String> sourceUrls,
        @JsonProperty("content_type") String contentType,
        String status,
        @JsonProperty("generated_content") Map<String, Object> generatedContent,
        @JsonProperty("suggested_image_prompt") String suggestedImagePrompt,
        @JsonProperty("error_message") String errorMessage,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt) {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String getSourceUrlsAsJsonString() {
        try {
            return sourceUrls != null ? objectMapper.writeValueAsString(sourceUrls) : "[]";
        } catch (IOException e) {

            System.err.println("Erro ao serializar sourceUrls: " + e.getMessage());
            return "[]";
        }
    }

    public String getGeneratedContentAsJsonString() {
        try {
            return generatedContent != null ? objectMapper.writeValueAsString(generatedContent) : "{}";
        } catch (IOException e) {

            System.err.println("Erro ao serializar generatedContent: " + e.getMessage());
            return "{}";
        }
    }
}
