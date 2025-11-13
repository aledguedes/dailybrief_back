package com.dailybrief.mapper;

import com.dailybrief.dto.*;
import com.dailybrief.dto.dashboard.DashboardPostResponseDTO;
import com.dailybrief.model.Image;
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
		post.setAuthor(postRequest.author());
		post.setTags(postRequest.tags());
		post.setMetaDescription(postRequest.metaDescription());
		post.setAffiliateLinks(postRequest.affiliateLinks());
		// category e status serão atribuídos no service
		// imagens também serão associadas no service
		return post;
	}

	// ---------- CONVERSÃO DE ENTIDADE PARA RESPONSE ----------
	public PostResponseDTO toResponse(Post post) {
		List<ImageResponseDTO> images = post.getImages().stream()
				.map(this::toImageResponseDTO)
				.collect(Collectors.toList());

		return new PostResponseDTO(
				post.getId(),
				post.getTitle(),
				post.getExcerpt(),
				post.getContent(),
				images, // agora é lista
				post.getAuthor(),
				post.getTags(),
				post.getCategory() != null ? toCategoryResponseDTO(post.getCategory()) : null,
				post.getMetaDescription(),
				post.getAffiliateLinks(),
				post.getStatus() != null ? toStatusDTO(post.getStatus()) : null,
				post.getPublishedAt() != null ? post.getPublishedAt().toString() : null,
				post.getReadTime(),
				post.getCreatedAt() != null ? post.getCreatedAt().toString() : null,
				post.getUpdatedAt() != null ? post.getUpdatedAt().toString() : null);
	}

	// ---------- AUXILIARES ----------
	private ImageResponseDTO toImageResponseDTO(Image image) {
		return new ImageResponseDTO(
				image.getId(),
				image.getUrl(),
				image.getPublicId());
	}

	private CategoryResponseDTO toCategoryResponseDTO(Category category) {
		return new CategoryResponseDTO(
				category.getId(),
				category.getName(),
				category.getDescription(),
				category.getTargetAudience());
	}

	private StatusDTO toStatusDTO(Status status) {
		return new StatusDTO(
				status.getId(),
				status.getName(),
				status.getDisplayName(),
				status.getBgClass(),
				status.getTextClass());
	}

	@SuppressWarnings("unused")
	private String getPreferredLanguage() {
		String lang = LocaleContextHolder.getLocale().getLanguage();
		return switch (lang) {
			case "en", "pt", "es" -> lang;
			default -> "pt";
		};
	}

	public LocalizedPostResponseDTO toLocalizedResponse(Post post) {
		if (post == null)
			return null;

		String lang = getPreferredLanguage();

		List<String> imageUrls = post.getImages() != null
				? post.getImages().stream()
						.map(Image::getUrl)
						.collect(Collectors.toList())
				: List.of();

		return new LocalizedPostResponseDTO(
				post.getId(),
				post.getTitle().getOrDefault(lang, post.getTitle().getOrDefault("pt", "")),
				post.getExcerpt().getOrDefault(lang, post.getExcerpt().getOrDefault("pt", "")),
				post.getContent().getOrDefault(lang, post.getContent().getOrDefault("pt", "")),
				imageUrls,
				post.getAuthor(),
				post.getTags(),
				post.getCategory() != null ? post.getCategory().getName() : null,
				post.getMetaDescription().getOrDefault(lang, post.getMetaDescription().getOrDefault("pt", "")),
				post.getAffiliateLinks().getOrDefault(lang, post.getAffiliateLinks().getOrDefault("pt", "")),
				post.getStatus() != null ? post.getStatus().getName() : null,
				post.getPublishedAt() != null ? post.getPublishedAt().toString() : null,
				post.getReadTime());
	}

	public List<LocalizedPostResponseDTO> toLocalizedResponseList(List<Post> posts) {
		return posts.stream().map(this::toLocalizedResponse).collect(Collectors.toList());
	}

	public DashboardPostResponseDTO toDashboardPostResponse(Post post) {
		String featuredImageUrl = null;

		if (post.getImages() != null && !post.getImages().isEmpty()) {
			// Pega a primeira imagem como destaque
			featuredImageUrl = post.getImages().get(0).getUrl();
		}

		return new DashboardPostResponseDTO(
				post.getId(),
				featuredImageUrl,
				post.getTitle(),
				post.getExcerpt(),
				post.getStatus() != null ? post.getStatus().getName() : null,
				post.getCategory() != null ? post.getCategory().getName() : null);
	}
}
