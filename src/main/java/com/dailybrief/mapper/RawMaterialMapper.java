package com.dailybrief.mapper;

import com.dailybrief.model.RawMaterial;
import com.dailybrief.dto.RawMaterialRequestDTO;
import com.dailybrief.dto.RawMaterialResponseDTO;
import com.dailybrief.model.Material;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RawMaterialMapper {

	int MAX_CONTENT_LENGTH = 1000;

	@Mapping(target = "material", source = "taskId", qualifiedByName = "mapTaskIdToMaterial")
	RawMaterial toEntity(RawMaterialRequestDTO request);

	@Mapping(target = "taskId", source = "material.taskId")
	@Mapping(target = "content", source = "content", qualifiedByName = "truncatedContent")
	RawMaterialResponseDTO toResponse(RawMaterial rawMaterial);

	@Mapping(target = "taskId", source = "material.taskId")
	RawMaterialResponseDTO toFullResponse(RawMaterial rawMaterial);

	@Named("mapTaskIdToMaterial")
	default Material mapTaskIdToMaterial(String taskId) {
		if (taskId == null) {
			return null;
		}
		Material material = new Material();
		material.setTaskId(taskId);
		return material;
	}

	@Named("truncatedContent")
	default String truncateContent(String content) {
		if (content == null || content.length() <= MAX_CONTENT_LENGTH) {
			return content;
		}

		return content.substring(0, MAX_CONTENT_LENGTH) + "...";
	}
}