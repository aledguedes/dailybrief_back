package com.dailybrief.mapper;

import com.dailybrief.dto.LocalizedPostResponseDTO;
import com.dailybrief.dto.PostRequestDTO;
import com.dailybrief.dto.PostResponseDTO;
import com.dailybrief.dto.dashboard.DashboardPostResponseDTO;
import com.dailybrief.model.Post;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    public Post toEntity(PostRequestDTO postRequest) {
        Post post = new Post();
        post.setTitle(postRequest.title());
        post.setContent(postRequest.content());
        post.setExcerpt(postRequest.excerpt());
        post.setMetaDescription(postRequest.metaDescription());
        post.setImage(postRequest.image());
        post.setAuthor(postRequest.author());
        post.setTags(postRequest.tags());
        post.setCategory(postRequest.category());
        post.setAffiliateLinks(postRequest.affiliateLinks());
        return post;
    }

    public PostResponseDTO toResponse(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getExcerpt(),
                post.getContent(),
                post.getImage(),
                post.getAuthor(),
                post.getTags(),
                post.getCategory(),
                post.getMetaDescription(),
                post.getAffiliateLinks(),
                post.getStatus().name(),
                post.getPublishedAt() != null ? post.getPublishedAt().toString() : null,
                post.getReadTime(),
                post.getCreatedAt() != null ? post.getCreatedAt().toString() : null,
                post.getUpdatedAt() != null ? post.getUpdatedAt().toString() : null);
    }

    public List<PostResponseDTO> toResponseList(List<Post> posts) {
        return posts.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public LocalizedPostResponseDTO toLocalizedResponse(Post post) {
        String lang = getPreferredLanguage();

        return new LocalizedPostResponseDTO(
            post.getId(),
            post.getTitle().getOrDefault(lang, post.getTitle().getOrDefault("pt", "")),
            post.getExcerpt().getOrDefault(lang, post.getExcerpt().getOrDefault("pt", "")),
            post.getContent().getOrDefault(lang, post.getContent().getOrDefault("pt", "")),
            post.getImage(),
            post.getAuthor(),
            post.getTags(),
            post.getCategory(),
            post.getMetaDescription().getOrDefault(lang, post.getMetaDescription().getOrDefault("pt", "")),
            post.getAffiliateLinks().getOrDefault(lang, post.getAffiliateLinks().getOrDefault("pt", "")),
            post.getStatus().name(),
            post.getPublishedAt() != null ? post.getPublishedAt().toString() : null,
            post.getReadTime()
        );
    }


    public List<LocalizedPostResponseDTO> toLocalizedResponseList(List<Post> posts) {
        return posts.stream().map(this::toLocalizedResponse).collect(Collectors.toList());
    }

    public DashboardPostResponseDTO toDashboardPostResponse(Post post) {
        return new DashboardPostResponseDTO(
                post.getId(),
                post.getImage(),
                post.getTitle(),
                post.getExcerpt(),
                post.getStatus().name(),
                post.getCategory());
    }

    private String getPreferredLanguage() {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        return switch (lang) {
            case "en", "pt", "es" -> lang;
            default -> "pt";
        };
    }
}