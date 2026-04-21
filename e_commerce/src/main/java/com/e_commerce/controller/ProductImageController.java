package com.e_commerce.controller;

import com.e_commerce.dto.ProductImageRequestDTO;
import com.e_commerce.dto.ProductImageResponseDTO;
import com.e_commerce.dto.ProductResponseDTO;
import com.e_commerce.service.IProductImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productImage")
@RequiredArgsConstructor
public class ProductImageController {

    private final IProductImageService productImageService;

    @GetMapping
    public ResponseEntity<List<ProductImageResponseDTO>> getAllProductImage() {
        List<ProductImageResponseDTO> productImageResponseDTOList = productImageService.findAllProductImage();
        return ResponseEntity.ok(productImageResponseDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductImageResponseDTO> getProductImageById(@PathVariable Long id) {
        ProductImageResponseDTO productImageResponseDTO = productImageService.findProductImageById(id);
        return ResponseEntity.ok(productImageResponseDTO);
    }

    @PostMapping
    public ResponseEntity<ProductImageResponseDTO> createProductImage(@RequestBody @Valid ProductImageRequestDTO productImageRequestDTO) {
        ProductImageResponseDTO productImageResponseDTO = productImageService.saveProductImage(productImageRequestDTO);
        return ResponseEntity.ok(productImageResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProductImageById(@PathVariable Long id) {
        productImageService.deleteProductImageById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductImageResponseDTO> updateProductImageById(@PathVariable Long id,@RequestBody @Valid ProductImageRequestDTO productImageRequestDTO) {
        ProductImageResponseDTO productImageResponseDTO = productImageService.updateProductImageById(id,productImageRequestDTO);
        return ResponseEntity.ok(productImageResponseDTO);
    }
}
