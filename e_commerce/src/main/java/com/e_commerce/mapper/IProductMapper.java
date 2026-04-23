package com.e_commerce.mapper;

import com.e_commerce.dto.*;
import com.e_commerce.model.Category;
import com.e_commerce.model.Product;
import com.e_commerce.model.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IProductMapper {

    public List<ProductResponseDTO> toProductResponseDTOList(List<Product> productList);

    public ProductResponseDTO toProductResponseDTO(Product product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    public Product toProduct(ProductRequestDTO productRequestDTO);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    public void updateProductFromDTO(ProductRequestDTO productRequestDTO,@MappingTarget Product product);

    CategoryResponseSimpleDTO toCategoryResponseDTO(Category category);

    ProductImageResponseDTO toProductImageResponseDTO(ProductImage productImage);


}
