package com.e_commerce.service;

import com.e_commerce.dto.ProductImageRequestDTO;
import com.e_commerce.dto.ProductImageResponseDTO;
import com.e_commerce.dto.ProductRequestDTO;
import com.e_commerce.dto.ProductResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.IProductImageMapper;
import com.e_commerce.model.ProductImage;
import com.e_commerce.repository.IProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService implements IProductImageService{

    private final IProductImageRepository productImageRepository;
    private final IProductImageMapper productImageMapper;

    @Override
    public List<ProductImageResponseDTO> getAllProductImage() {
        List<ProductImage> productImagesList = productImageRepository.findAll();
        return productImageMapper.toProductImageResponseDTOList(productImagesList);
    }

    @Override
    public ProductImageResponseDTO getFindProductImageById(Long id) {
        ProductImage productImage = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage '"+ id +"' not found."));
        return productImageMapper.toProductImageResponseDTO(productImage);
    }

    @Override
    public ProductImageResponseDTO saveProductImage(ProductImageRequestDTO productImageRequestDTO) {
        ProductImage productImage = productImageMapper.toProductImage(productImageRequestDTO);
        ProductImage savedProductImage = productImageRepository.save(productImage);
        return productImageMapper.toProductImageResponseDTO(savedProductImage);
    }

    @Override
    public void deleteProductImageById(Long id) {
        if(!productImageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id '" + id + "' not found.");
        }
        productImageRepository.deleteById(id);
    }

    @Override
    public ProductImageResponseDTO updateProductImageById(Long id, ProductImageRequestDTO productImageRequestDTO) {
        ProductImage foundProductImage = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage '"+ id +"' not found."));
        productImageMapper.updateProductImageAppFromDTO(productImageRequestDTO,foundProductImage);
        ProductImage updateProductImage = productImageRepository.save(foundProductImage);
        return productImageMapper.toProductImageResponseDTO(updateProductImage);
    }
}
