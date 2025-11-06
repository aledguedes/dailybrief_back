package com.dailybrief.mapper;

import com.dailybrief.model.Material;
import com.dailybrief.dto.MaterialResponseDTO;
import com.dailybrief.model.Status;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = StatusMapper.class)
public interface MaterialMapper {

    MaterialResponseDTO toResponse(Material material);

    @Named("mapStatusIdToStatus")
    default Status mapStatusIdToStatus(Integer statusId) {
        if (statusId == null)
            return null;
        Status status = new Status();
        status.setId(statusId);
        return status;
    }
}
