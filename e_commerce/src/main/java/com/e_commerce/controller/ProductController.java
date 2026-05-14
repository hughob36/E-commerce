package com.e_commerce.controller;

import com.e_commerce.dto.ProductRequestDTO;
import com.e_commerce.dto.ProductResponseDTO;
import com.e_commerce.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
public class ProductController {

    private final IProductService productService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponseDTO>> getAllProduct() {
        List<ProductResponseDTO> productResponseDTOList = productService.findAll();
        return ResponseEntity.ok(productResponseDTOList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO productResponseDTO = productService.findById(id);
        return ResponseEntity.ok(productResponseDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO) {
        ProductResponseDTO saveProductResponseDTO = productService.save(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveProductResponseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity deleteProductById(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> updateProductById(@PathVariable Long id, @RequestBody @Valid ProductRequestDTO productRequestDTO) {
        ProductResponseDTO productResponseDTO = productService.update(id,productRequestDTO);
        return ResponseEntity.ok(productResponseDTO);
    }


}
