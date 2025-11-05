package com.dailybrief.mapper;

import com.dailybrief.dto.CategoryRequestDTO;
import com.dailybrief.dto.CategoryResponseDTO;
import com.dailybrief.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    
    Category toEntity(CategoryRequestDTO request);
    
    CategoryResponseDTO toResponse(Category category);
    
    void updateEntityFromDto(CategoryRequestDTO dto, @MappingTarget Category category);
}