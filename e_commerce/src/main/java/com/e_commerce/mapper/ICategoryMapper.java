package com.e_commerce.mapper;

import com.e_commerce.dto.CategoryRequestDTO;
import com.e_commerce.dto.CategoryResponseDTO;
import com.e_commerce.dto.CategoryResponseSimpleDTO;
import com.e_commerce.dto.ProductResponseSimpleDTO;
import com.e_commerce.model.Category;
import com.e_commerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {

    public List<CategoryResponseDTO> toCategoryResponseDTOList(List<Category> categoryList);

    // RESALTADO: Mapeo explícito de las listas
    // Esto obliga a MapStruct a usar los métodos de abajo para cada lista
    //@Mapping(target = "subCategories", source = "subCategories")
    //@Mapping(target = "products", source = "products")
    public CategoryResponseDTO toCategoryResponseDTO(Category category);

    @Mapping(target = "parentCategory", ignore = true)
    @Mapping(target = "subCategories", ignore = true)
    @Mapping(target = "products", ignore = true)
    public Category toCategory(CategoryRequestDTO categoryRequestDTO);
    public void updateCategoryFromDTO(CategoryRequestDTO categoryRequestDTO,@MappingTarget Category category);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "sku", source = "sku")
    @Mapping(target = "imageUrl", source = "imageUrl")
    public ProductResponseSimpleDTO toProductResponseDTO(Product product);

    CategoryResponseSimpleDTO toCategoryResponseSimpleDTO(Category category);

}
