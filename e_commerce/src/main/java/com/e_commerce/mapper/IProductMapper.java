package com.e_commerce.mapper;

import com.e_commerce.dto.ProductRequestDTO;
import com.e_commerce.dto.ProductResponseDTO;
import com.e_commerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IProductMapper {

    public List<ProductResponseDTO> toProductResponseDTOList(List<Product> productList);
    public ProductResponseDTO toProductResponseDTO(Product product);
    public Product toProduct(ProductRequestDTO productRequestDTO);
    public void updateProductFromDTO(ProductRequestDTO productRequestDTO,@MappingTarget Product product);
}
