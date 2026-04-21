package com.e_commerce.mapper;

import com.e_commerce.dto.ProductImageRequestDTO;
import com.e_commerce.dto.ProductImageResponseDTO;
import com.e_commerce.model.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IProductImageMapper {

    public List<ProductImageResponseDTO> toProductImageResponseDTOList(List<ProductImage> productImageList);
    public ProductImageResponseDTO toProductImageResponseDTO(ProductImage productImage);
    public ProductImage toProductImage(ProductImageRequestDTO ProductImageRequestDTO);
    public void updateProductImageAppFromDTO(ProductImageRequestDTO productImageRequestDTO, @MappingTarget ProductImage productImage);
}
