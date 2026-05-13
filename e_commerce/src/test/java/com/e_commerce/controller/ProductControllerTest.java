package com.e_commerce.controller;

import com.e_commerce.dto.CategoryResponseSimpleDTO;
import com.e_commerce.dto.ProductImageResponseDTO;
import com.e_commerce.dto.ProductResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.service.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;


import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/product - Should return list of products with 200 OK when data exists")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getAllProduct_shouldReturnProductResponseDTOList() throws Exception {

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

        when(productService.findAll()).thenReturn(productResponseDTOList);

        mockMvc.perform(get("/api/product").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("product1"))
                .andExpect(jsonPath("$[0].description").value("product prueba"))
                .andExpect(jsonPath("$[0].price").value(100.00))
                .andExpect(jsonPath("$[0].stock").value(10))
                .andExpect(jsonPath("$[0].sku").value("pru1"))
                .andExpect(jsonPath("$[0].category").exists())
                .andExpect(jsonPath("$[0].imageUrl").value("www.prueba.com"))
                .andExpect(jsonPath("$[0].isActive").value(true))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].images").isArray())
                .andExpect(jsonPath("$[0].images", not(empty())));

        verify(productService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/product - Should return 401 Unauthorized when no user is authenticated")
    public void getAllRole_WithoutUser_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/product"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/product/{id} - Should return product details with 200 OK when ID exists")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getProductById_shouldReturnProductResponseDTO() throws Exception {

        Long id = 1L;
        CategoryResponseSimpleDTO categoryResponseSimpleDTO = new CategoryResponseSimpleDTO();
        ProductImageResponseDTO productImgRes = new ProductImageResponseDTO();
        List<ProductImageResponseDTO> productImageResponseDTOList = List.of(productImgRes);

        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .id(id)
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

        when(productService.findById(id)).thenReturn(productResponseDTO);

        mockMvc.perform(get("/api/product/{id}",id).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("product1"))
                .andExpect(jsonPath("$.description").value("product prueba"))
                .andExpect(jsonPath("$.price").value(100.00))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.sku").value("pru1"))
                .andExpect(jsonPath("$.category").exists())
                .andExpect(jsonPath("$.imageUrl").value("www.prueba.com"))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.images").isArray())
                .andExpect(jsonPath("$.images", not(empty())));

        verify(productService, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/product/{id} - Should return 404 Not Found when permission ID does not exist")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getProductById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {

        Long id = 99L;
        when(productService.findById(id)).thenThrow(new ResourceNotFoundException("Product '"+ id +"' not found."));

        mockMvc.perform(get("/api/product/{id}", id))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).findById(id);
    }
}
