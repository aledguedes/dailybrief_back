package com.dailybrief.mapper;

import com.dailybrief.dto.LocalizedPostResponseDTO;
import com.dailybrief.dto.PostRequestDTO;
import com.dailybrief.dto.PostResponseDTO;
import com.dailybrief.dto.dashboard.DashboardPostResponseDTO;
import com.dailybrief.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PostMapper {

	private static final Logger logger = LoggerFactory.getLogger(PostMapper.class);

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
		return new PostResponseDTO(post.getId(), post.getTitle(), post.getExcerpt(), post.getContent(), post.getImage(),
				post.getAuthor(), post.getTags(), post.getCategory(), post.getMetaDescription(),
				post.getAffiliateLinks(), post.getStatus().name(),
				post.getPublishedAt() != null ? post.getPublishedAt().toString() : null, post.getReadTime(),
				post.getCreatedAt() != null ? post.getCreatedAt().toString() : null,
				post.getUpdatedAt() != null ? post.getUpdatedAt().toString() : null);
	}

	public List<PostResponseDTO> toResponseList(List<Post> posts) {
		return posts.stream().map(this::toResponse).collect(Collectors.toList());
	}

	public LocalizedPostResponseDTO toLocalizedResponse(Post post) {
		return toLocalizedResponse(post, null);
	}

	public LocalizedPostResponseDTO toLocalizedResponse(Post post, String lang) {
		String effectiveLang = getPreferredLanguage(lang);
		logger.info("Mapeando post ID {} para o idioma: {}", post.getId(), effectiveLang);
		logger.debug(
				"Dados brutos do post - Título: {}, Resumo: {}, Conteúdo: {}, Meta Descrição: {}, Links de Afiliados: {}",
				post.getTitle(), post.getExcerpt(), post.getContent(), post.getMetaDescription(),
				post.getAffiliateLinks());

		return new LocalizedPostResponseDTO(post.getId(), getLocalizedValue(post.getTitle(), effectiveLang, "Título"),
				getLocalizedValue(post.getExcerpt(), effectiveLang, "Resumo"),
				getLocalizedValue(post.getContent(), effectiveLang, "Conteúdo"), post.getImage(), post.getAuthor(),
				post.getTags(), post.getCategory(),
				getLocalizedValue(post.getMetaDescription(), effectiveLang, "Meta Descrição"),
				getLocalizedValue(post.getAffiliateLinks(), effectiveLang, "Links de Afiliados"),
				post.getStatus().name(), post.getPublishedAt() != null ? post.getPublishedAt().toString() : null,
				post.getReadTime());
	}

	public List<LocalizedPostResponseDTO> toLocalizedResponseList(List<Post> posts) {
		return toLocalizedResponseList(posts, null);
	}

	public List<LocalizedPostResponseDTO> toLocalizedResponseList(List<Post> posts, String lang) {
		return posts.stream().map(post -> toLocalizedResponse(post, lang)).collect(Collectors.toList());
	}

	public DashboardPostResponseDTO toDashboardPostResponse(Post post) {
		return new DashboardPostResponseDTO(post.getId(), post.getImage(), post.getTitle(), post.getExcerpt(),
				post.getStatus().name(), post.getCategory());
	}

	private String getLocalizedValue(Map<String, String> map, String lang, String fieldName) {
		if (map == null) {
			logger.warn("{} nulo para post no idioma {}. Retornando string vazia.", fieldName, lang);
			return "";
		}
		if (map.isEmpty()) {
			logger.warn("{} vazio para post no idioma {}. Retornando string vazia.", fieldName, lang);
			return "";
		}
		// Tenta a chave exata primeiro
		String value = map.getOrDefault(lang, null);
		if (value != null && !value.isEmpty()) {
			logger.debug("Valor selecionado em {} para idioma {}: {}", fieldName, lang, value);
			return value;
		}
		// Tenta case-insensitive
		for (String key : map.keySet()) {
			if (key.equalsIgnoreCase(lang)) {
				value = map.get(key);
				if (!value.isEmpty()) {
					logger.debug("Valor selecionado em {} para idioma {} (case-insensitive): {}", fieldName, lang,
							value);
					return value;
				}
			}
		}
		// Fallback para 'pt' ou 'PT'
		value = map.getOrDefault("pt", map.getOrDefault("PT", ""));
		if (value.isEmpty()) {
			logger.warn("Nenhum valor encontrado em {} para idioma {} ou fallback 'pt'/'PT'. Mapa: {}", fieldName, lang,
					map);
		} else {
			logger.debug("Valor selecionado em {} para fallback 'pt'/'PT': {}", fieldName, value);
		}
		return value;
	}

	private String getPreferredLanguage(String lang) {
		if (lang != null && !lang.isEmpty()) {
			logger.info("Usando idioma fornecido: {}", lang);
			if (lang.toLowerCase().startsWith("pt")) {
				logger.debug("Mapeando idioma '{}' para 'pt'", lang);
				return "pt";
			}
			return switch (lang.toLowerCase()) {
			case "en", "es" -> lang.toLowerCase();
			default -> {
				logger.warn("Idioma fornecido não suportado '{}'. Usando fallback 'pt'.", lang);
				yield "pt";
			}
			};
		}
		String localeLang = LocaleContextHolder.getLocale().toLanguageTag().toLowerCase();
		logger.info("Nenhum idioma fornecido. Usando LocaleContextHolder: {}", localeLang);
		if (localeLang.startsWith("pt")) {
			logger.debug("Mapeando idioma '{}' para 'pt'", localeLang);
			return "pt";
		}
		return switch (localeLang) {
		case "en", "es" -> localeLang;
		default -> {
			logger.warn("Idioma do LocaleContextHolder não suportado '{}'. Usando fallback 'pt'.", localeLang);
			yield "pt";
		}
		};
	}
}