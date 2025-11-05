package com.dailybrief.service.impl;

import com.dailybrief.dto.CategoryRequestDTO;
import com.dailybrief.dto.CategoryResponseDTO;
import com.dailybrief.mapper.CategoryMapper;
import com.dailybrief.model.Category;
import com.dailybrief.repository.CategoryRepository;
import com.dailybrief.service.CategoryService;
import com.dailybrief.exception.PostNotFoundException; // Reutilizando para 404
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;

	public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
		this.categoryRepository = categoryRepository;
		this.categoryMapper = categoryMapper;
	}

	@Override
	@Transactional
	public CategoryResponseDTO createCategory(CategoryRequestDTO request) {
		Category category = categoryMapper.toEntity(request);
		Category savedCategory = categoryRepository.save(category);
		return categoryMapper.toResponse(savedCategory);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryResponseDTO> getAllCategories() {
		return categoryRepository.findAll().stream().map(categoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryResponseDTO getCategoryById(Integer id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new PostNotFoundException("Category not found with ID: " + id));
		return categoryMapper.toResponse(category);
	}

	@Override
	@Transactional
	public CategoryResponseDTO updateCategory(Integer id, CategoryRequestDTO request) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new PostNotFoundException("Category not found with ID: " + id));

		categoryMapper.updateEntityFromDto(request, category);
		Category updatedCategory = categoryRepository.save(category);
		return categoryMapper.toResponse(updatedCategory);
	}

	@Override
	@Transactional
	public void deleteCategory(Integer id) {
		if (!categoryRepository.existsById(id)) {
			throw new PostNotFoundException("Category not found with ID: " + id);
		}
		categoryRepository.deleteById(id);
	}
}