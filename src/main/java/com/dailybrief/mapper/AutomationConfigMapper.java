package com.dailybrief.mapper;

import com.dailybrief.model.AutomationConfig;
import com.dailybrief.dto.AutomationConfigRequestDTO;
import com.dailybrief.dto.AutomationConfigResponseDTO;
import com.dailybrief.model.Status;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = StatusMapper.class)
public interface AutomationConfigMapper {

    // Request → Entity
    @Mapping(target = "status", source = "statusId", qualifiedByName = "mapStatusIdToStatus")
    AutomationConfig toEntity(AutomationConfigRequestDTO request);

    // Entity → Response
    AutomationConfigResponseDTO toResponse(AutomationConfig entity);

    // Conversão auxiliar: statusId → Status
    @Named("mapStatusIdToStatus")
    default Status mapStatusIdToStatus(Integer statusId) {
        if (statusId == null) {
            return null;
        }
        Status status = new Status();
        status.setId(statusId);
        return status;
    }
}
