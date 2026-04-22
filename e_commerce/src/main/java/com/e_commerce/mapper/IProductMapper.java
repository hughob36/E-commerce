package com.e_commerce.mapper;

import com.e_commerce.dto.CategoryResponseDTO;
import com.e_commerce.dto.ProductImageResponseDTO;
import com.e_commerce.dto.ProductRequestDTO;
import com.e_commerce.dto.ProductResponseDTO;
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


    // 2. Definimos estos métodos "ayudantes" para que MapStruct los use
    // internamente al mapear el ProductResponseDTO
    @Mapping(target = "products", ignore = true)
    CategoryResponseDTO toCategoryResponseDTO(Category category);

    ProductImageResponseDTO toProductImageResponseDTO(ProductImage productImage);


}
