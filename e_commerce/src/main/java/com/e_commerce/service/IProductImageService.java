package com.e_commerce.service;

import com.e_commerce.dto.ProductImageRequestDTO;
import com.e_commerce.dto.ProductImageResponseDTO;

import java.util.List;

public interface IProductImageService {

    public List<ProductImageResponseDTO> getAllProductImage();
    public ProductImageResponseDTO getFindProductImageById(Long id);
    public ProductImageResponseDTO saveProductImage(ProductImageRequestDTO productImageRequestDTO);
    public void deleteProductImageById(Long id);
    public ProductImageResponseDTO updateProductImageById(Long id, ProductImageRequestDTO productImageRequestDTO);
}
