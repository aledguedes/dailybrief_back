package com.dailybrief.mapper;

import com.dailybrief.model.Status;
import com.dailybrief.dto.StatusDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusMapper {

    StatusDTO toResponse(Status status);
}
