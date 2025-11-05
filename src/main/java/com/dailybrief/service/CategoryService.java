package com.dailybrief.service;

import com.dailybrief.dto.CategoryRequestDTO;
import com.dailybrief.dto.CategoryResponseDTO;
import java.util.List;

public interface CategoryService {
	CategoryResponseDTO createCategory(CategoryRequestDTO request);

	List<CategoryResponseDTO> getAllCategories();

	CategoryResponseDTO getCategoryById(Integer id);

	CategoryResponseDTO updateCategory(Integer id, CategoryRequestDTO request);

	void deleteCategory(Integer id);
}