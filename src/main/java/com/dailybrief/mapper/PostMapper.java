package com.dailybrief.mapper;

import com.dailybrief.dto.PostRequestDTO;
import com.dailybrief.dto.PostResponseDTO;
import com.dailybrief.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "status", ignore = true) // Status é definido no serviço
    @Mapping(target = "publishedAt", expression = "java(request.publishedAt() != null ? java.time.Instant.parse(request.publishedAt()) : null)")
    Post toEntity(PostRequestDTO request);

    @Mapping(target = "date", expression = "java(post.getPublishedAt() != null ? post.getPublishedAt().toString() : null)")
    PostResponseDTO toResponse(Post post);

    List<PostResponseDTO> toResponseList(List<Post> posts);
}