package com.e_commerce.service;

import com.e_commerce.dto.CategoryRequestDTO;
import com.e_commerce.dto.CategoryResponseDTO;
import com.e_commerce.model.Category;

import java.util.List;

public interface ICategoryService {

    public List<CategoryResponseDTO> getAllCategory();
    public CategoryResponseDTO getCategoryById(Long id);
    public CategoryResponseDTO saveCategory(CategoryRequestDTO categoryRequestDTO);
    public void deleteCategoryById(Long id);
    public CategoryResponseDTO updateCategoryById(Long id, CategoryRequestDTO categoryRequestDTO);
}
