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

    //@Mapping(target = "product", ignore = true)
    @Mapping(target = "product", expression = "java(toProductResponseSimpleDTO(productImage.getProduct()))")
    public ProductImageResponseDTO toProductImageResponseDTO(ProductImage productImage);

    @Mapping(target = "product", ignore = true)
    public ProductImage toProductImage(ProductImageRequestDTO ProductImageRequestDTO);

    @Mapping(target = "product", ignore = true)
    public void updateProductImageAppFromDTO(ProductImageRequestDTO productImageRequestDTO, @MappingTarget ProductImage productImage);


    //public ProductResponseSimpleDTO toProductResponseDTO(Product product);

    // 2. Este metodo es el que CREA el DTO simple sin las listas conflictivas
    default ProductResponseSimpleDTO toProductResponseSimpleDTO(Product product) {
        if (product == null) return null;
        return new ProductResponseSimpleDTO(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getImageUrl()
        );
    }

}
