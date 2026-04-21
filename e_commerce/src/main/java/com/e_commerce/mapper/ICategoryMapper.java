package com.e_commerce.mapper;

import com.e_commerce.dto.CategoryRequestDTO;
import com.e_commerce.dto.CategoryResponseDTO;
import com.e_commerce.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {

    public List<CategoryResponseDTO> toCategoryResponseDTOList(List<Category> categoryList);
    public CategoryResponseDTO toCategoryResponseDTO(Category category);
    public Category toCategory(CategoryRequestDTO categoryRequestDTO);
    public void updateCategoryFromDTO(CategoryRequestDTO categoryRequestDTO,@MappingTarget Category category);

}
