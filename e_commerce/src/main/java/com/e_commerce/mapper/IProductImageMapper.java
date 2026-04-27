package com.e_commerce.mapper;

import com.e_commerce.dto.ProductImageRequestDTO;
import com.e_commerce.dto.ProductImageResponseDTO;
import com.e_commerce.dto.ProductResponseDTO;
import com.e_commerce.dto.ProductResponseSimpleDTO;
import com.e_commerce.model.Product;
import com.e_commerce.model.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IProductImageMapper {

    public List<ProductImageResponseDTO> toProductImageResponseDTOList(List<ProductImage> productImageList);

    public ProductImageResponseDTO toProductImageResponseDTO(ProductImage productImage);

    @Mapping(target = "product", ignore = true)
    public ProductImage toProductImage(ProductImageRequestDTO ProductImageRequestDTO);

    @Mapping(target = "product", ignore = true)
    public void updateProductImageAppFromDTO(ProductImageRequestDTO productImageRequestDTO, @MappingTarget ProductImage productImage);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "sku", source = "sku")
    @Mapping(target = "imageUrl", source = "imageUrl")
    public ProductResponseSimpleDTO toProductResponseDTO(Product product);

}
