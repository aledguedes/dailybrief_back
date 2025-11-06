package com.dailybrief.mapper;

import com.dailybrief.dto.TrendingTopicSuggestionDTO;
import com.dailybrief.model.PostStatus;
import com.dailybrief.model.TrendingTopicSuggestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TrendingTopicSuggestionMapper {

    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    TrendingTopicSuggestionDTO toDTO(TrendingTopicSuggestion entity);

    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    TrendingTopicSuggestion toEntity(TrendingTopicSuggestionDTO dto);

    @Named("statusToString")
    default String statusToString(PostStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("stringToStatus")
    default PostStatus stringToStatus(String status) {
        return status != null ? PostStatus.valueOf(status) : null;
    }
}
