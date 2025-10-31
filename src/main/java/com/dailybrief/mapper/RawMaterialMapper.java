package com.dailybrief.mapper;

import com.dailybrief.model.RawMaterial;
import com.dailybrief.dto.RawMaterialRequestDTO;
import com.dailybrief.dto.RawMaterialResponseDTO;
import com.dailybrief.model.Material;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RawMaterialMapper {

    @Mapping(target = "material", source = "taskId", qualifiedByName = "mapTaskIdToMaterial")
    RawMaterial toEntity(RawMaterialRequestDTO request);

    @Mapping(target = "taskId", source = "material.taskId")
    RawMaterialResponseDTO toResponse(RawMaterial rawMaterial);

    @Named("mapTaskIdToMaterial")
    default Material mapTaskIdToMaterial(String taskId) {
        if (taskId == null) {
            return null;
        }
        Material material = new Material();
        material.setTaskId(taskId);
        return material;
    }
}
