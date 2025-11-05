package com.dailybrief.mapper;

import com.dailybrief.dto.*;
import com.dailybrief.dto.dashboard.DashboardPostResponseDTO;
import com.dailybrief.model.Post;
import com.dailybrief.model.Category;
import com.dailybrief.model.Status;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    // ---------- CONVERSÃO DE REQUEST PARA ENTIDADE ----------
    public Post toEntity(PostRequestDTO postRequest) {
        Post post = new Post();
        post.setTitle(postRequest.title());
        post.setExcerpt(postRequest.excerpt());
        post.setContent(postRequest.content());
        post.setImage(postRequest.image());
        post.setAuthor(postRequest.author());
        post.setTags(postRequest.tags());
        post.setMetaDescription(postRequest.metaDescription());
        post.setAffiliateLinks(postRequest.affiliateLinks());
        // categoria e status serão atribuídos no service
        return post;
    }

    // ---------- CONVERSÃO DE ENTIDADE PARA RESPONSE ----------
    public PostResponseDTO toResponse(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getExcerpt(),
                post.getContent(),
                post.getImage(),
                post.getAuthor(),
                post.getTags(),
                post.getCategory() != null ? toCategoryResponseDTO(post.getCategory()) : null,
                post.getMetaDescription(),
                post.getAffiliateLinks(),
                post.getStatus() != null ? toStatusDTO(post.getStatus()) : null,
                post.getPublishedAt() != null ? post.getPublishedAt().toString() : null,
                post.getReadTime(),
                post.getCreatedAt() != null ? post.getCreatedAt().toString() : null,
                post.getUpdatedAt() != null ? post.getUpdatedAt().toString() : null
        );
    }

    public List<PostResponseDTO> toResponseList(List<Post> posts) {
        return posts.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ---------- CONVERSÃO PARA LOCALIZED RESPONSE ----------
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
                post.getCategory() != null ? post.getCategory().getName() : null,
                post.getMetaDescription().getOrDefault(lang, post.getMetaDescription().getOrDefault("pt", "")),
                post.getAffiliateLinks().getOrDefault(lang, post.getAffiliateLinks().getOrDefault("pt", "")),
                post.getStatus() != null ? post.getStatus().getName() : null,
                post.getPublishedAt() != null ? post.getPublishedAt().toString() : null,
                post.getReadTime()
        );
    }

    public List<LocalizedPostResponseDTO> toLocalizedResponseList(List<Post> posts) {
        return posts.stream().map(this::toLocalizedResponse).collect(Collectors.toList());
    }

    // ---------- DASHBOARD POST RESPONSE ----------
    public DashboardPostResponseDTO toDashboardPostResponse(Post post) {
        return new DashboardPostResponseDTO(
                post.getId(),
                post.getImage(),
                post.getTitle(),
                post.getExcerpt(),
                post.getStatus() != null ? post.getStatus().getName() : null,
                post.getCategory() != null ? post.getCategory().getName() : null
        );
    }

    // ---------- AUXILIARES ----------
    private String getPreferredLanguage() {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        return switch (lang) {
            case "en", "pt", "es" -> lang;
            default -> "pt";
        };
    }

    private CategoryResponseDTO toCategoryResponseDTO(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getTargetAudience()
        );
    }

    private StatusDTO toStatusDTO(Status status) {
        return new StatusDTO(
                status.getId(),
                status.getName(),
                status.getDisplayName(),
                status.getBgClass(),
                status.getTextClass()
        );
    }
}
