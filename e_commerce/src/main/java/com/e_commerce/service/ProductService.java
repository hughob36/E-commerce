package com.e_commerce.service;

import com.e_commerce.dto.CategoryResponseDTO;
import com.e_commerce.dto.ProductImageResponseDTO;
import com.e_commerce.dto.ProductRequestDTO;
import com.e_commerce.dto.ProductResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.ICategoryMapper;
import com.e_commerce.mapper.IProductImageMapper;
import com.e_commerce.mapper.IProductMapper;
import com.e_commerce.model.Category;
import com.e_commerce.model.Product;
import com.e_commerce.model.ProductImage;
import com.e_commerce.repository.ICategoryRepository;
import com.e_commerce.repository.IProductImageRepository;
import com.e_commerce.repository.IProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final IProductRepository productRepository;
    private final IProductMapper productMapper;
    private final ICategoryRepository categoryRepository;
    private final ICategoryMapper categoryMapper;
    private final IProductImageRepository productImageRepository;
    private final IProductImageMapper productImageMapper;

    @Override
    public List<ProductResponseDTO> findAll() {
        List<Product> productList = productRepository.findAll();
        return productMapper.toProductResponseDTOList(productList);
    }

    @Override
    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product '"+ id +"' not found."));
        return productMapper.toProductResponseDTO(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO save(ProductRequestDTO productRequestDTO) {

        Product product = productMapper.toProduct(productRequestDTO);

        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Product/Category '"+ productRequestDTO.getCategoryId() +"' not found."));
        product.setCategory(category);


        // 3. Hidratar Imagenes (Consultando todas de una vez)
        if (productRequestDTO.getImagesId() != null && !productRequestDTO.getImagesId().isEmpty()) {
            List<ProductImage> images = productImageRepository.findAllById(productRequestDTO.getImagesId());
            // Validamos si falto alguna imagen por encontrar
            if (images.size() != productRequestDTO.getImagesId().size()) {
                throw new ResourceNotFoundException("One or more image IDs not found");
            }
            // Seteamos la relación bidireccional si es necesario
            images.forEach(img -> img.setProduct(product));
            product.setImages(images);
        }
        Product savedProduct = productRepository.save(product);

        return productMapper.toProductResponseDTO(savedProduct);
    }

    @Override
    public void deleteById(Long id) {
        if(!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id '" + id + "' not found.");
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO productRequestDTO) {
        Product foundProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product '"+ id +"' not found."));
        productMapper.updateProductFromDTO(productRequestDTO,foundProduct);
        Product updateProduct = productRepository.save(foundProduct);
        return productMapper.toProductResponseDTO(updateProduct);
    }
}
