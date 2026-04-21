package com.e_commerce.service;

import com.e_commerce.dto.CategoryRequestDTO;
import com.e_commerce.dto.CategoryResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.ICategoryMapper;
import com.e_commerce.model.Category;
import com.e_commerce.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    private final ICategoryRepository categoryRepository;
    private final ICategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDTO> getAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryMapper.toCategoryResponseDTOList(categoryList);
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category '"+ id +"' not found."));
        return categoryMapper.toCategoryResponseDTO(foundCategory);
    }

    @Override
    public CategoryResponseDTO saveCategory(CategoryRequestDTO categoryRequestDTO) {
        Category categoryRequest = categoryMapper.toCategory(categoryRequestDTO);
        Category saveCategory = categoryRepository.save(categoryRequest);
        return categoryMapper.toCategoryResponseDTO(saveCategory);
    }

    @Override
    public void deleteCategoryById(Long id) {
        if(!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id '" + id + "' not found.");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponseDTO updateCategoryById(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category '"+ id +"' not found."));
        categoryMapper.updateCategoryFromDTO(categoryRequestDTO,foundCategory);
        Category updateCategory = categoryRepository.save(foundCategory);
        return categoryMapper.toCategoryResponseDTO(updateCategory);
    }
}
