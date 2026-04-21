package com.e_commerce.service;

import com.e_commerce.dto.ProductImageRequestDTO;
import com.e_commerce.dto.ProductImageResponseDTO;

import java.util.List;

public interface IProductImageService {

    public List<ProductImageResponseDTO> findAllProductImage();
    public ProductImageResponseDTO findProductImageById(Long id);
    public ProductImageResponseDTO saveProductImage(ProductImageRequestDTO productImageRequestDTO);
    public void deleteProductImageById(Long id);
    public ProductImageResponseDTO updateProductImageById(Long id, ProductImageRequestDTO productImageRequestDTO);
}
