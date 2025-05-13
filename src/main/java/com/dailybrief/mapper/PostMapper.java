package com.dailybrief.mapper;

import com.dailybrief.dto.LocalizedPostResponseDTO;
import com.dailybrief.dto.PostRequestDTO;
import com.dailybrief.dto.PostResponseDTO;
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
                post.getReadTime());
    }

    public List<PostResponseDTO> toResponseList(List<Post> posts) {
        return posts.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public LocalizedPostResponseDTO toLocalizedResponse(Post post) {
        String lang = getPreferredLanguage();
        LocalizedPostResponseDTO dto = new LocalizedPostResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle().getOrDefault(lang, post.getTitle().getOrDefault("pt", "")));
        dto.setExcerpt(post.getExcerpt().getOrDefault(lang, post.getExcerpt().getOrDefault("pt", "")));
        dto.setContent(post.getContent().getOrDefault(lang, post.getContent().getOrDefault("pt", "")));
        dto.setImage(post.getImage());
        dto.setAuthor(post.getAuthor());
        dto.setTags(post.getTags());
        dto.setCategory(post.getCategory());
        dto.setMetaDescription(
                post.getMetaDescription().getOrDefault(lang, post.getMetaDescription().getOrDefault("pt", "")));
        dto.setAffiliateLinks(
                post.getAffiliateLinks().getOrDefault(lang, post.getAffiliateLinks().getOrDefault("pt", "")));
        dto.setStatus(post.getStatus().name());
        dto.setDate(post.getPublishedAt() != null ? post.getPublishedAt().toString() : null);
        dto.setReadTime(post.getReadTime());
        return dto;
    }

    public List<LocalizedPostResponseDTO> toLocalizedResponseList(List<Post> posts) {
        return posts.stream().map(this::toLocalizedResponse).collect(Collectors.toList());
    }

    private String getPreferredLanguage() {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        return switch (lang) {
            case "en", "pt", "es" -> lang;
            default -> "pt"; // Padrão
        };
    }
}