package com.e_commerce.service;

import com.e_commerce.dto.CategoryResponseSimpleDTO;
import com.e_commerce.dto.ProductImageResponseDTO;
import com.e_commerce.dto.ProductResponseDTO;
import com.e_commerce.mapper.IProductMapper;
import com.e_commerce.model.Category;
import com.e_commerce.model.Product;
import com.e_commerce.model.ProductImage;
import com.e_commerce.repository.IProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private IProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("Should return a list of ProductResponseDTO when findAll is called")
    public void findAll_shouldReturnProductResponseDTOList() {

        Category category = Category.builder()
                .id(1L)
                .name("electric")
                .createdAt(LocalDateTime.now())
                .build();

        ProductImage productImg = new ProductImage();
        List<ProductImage> productImgList = List.of(productImg);

        Product product = Product.builder()
                .id(1L)
                .name("product1")
                .description("product prueba")
                .price(new BigDecimal("100.00"))
                .stock(10)
                .sku("pru1")
                .category(category)
                .imageUrl("www.prueba.com")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .images(productImgList)
                .build();
        List<Product> productList = List.of(product);

        CategoryResponseSimpleDTO categoryResponseSimpleDTO = new CategoryResponseSimpleDTO();
        ProductImageResponseDTO productImgRes = new ProductImageResponseDTO();
        List<ProductImageResponseDTO> productImageResponseDTOList = List.of(productImgRes);

        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .id(1L)
                .name("product1")
                .description("product prueba")
                .price(new BigDecimal("100.00"))
                .stock(10)
                .sku("pru1")
                .category(categoryResponseSimpleDTO)
                .imageUrl("www.prueba.com")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .images(productImageResponseDTOList)
                .build();
        List<ProductResponseDTO> productResponseDTOList = List.of(productResponseDTO);

        when(productRepository.findAll()).thenReturn(productList);
        when(productMapper.toProductResponseDTOList(productList)).thenReturn(productResponseDTOList);

        List<ProductResponseDTO> result = productService.findAll();

        assertAll("Verify response properties",
                () -> assertNotNull(result, "The result should not be null"),
                () -> assertEquals(1, result.size(), "The list size should be 1"),
                () -> assertEquals("product1", result.get(0).getName(), "The role name does not match")
        );
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(1)).toProductResponseDTOList(anyList());
        verifyNoMoreInteractions(productRepository,productMapper);
    }

}
