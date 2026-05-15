package com.e_commerce.controller;

import com.e_commerce.dto.CategoryResponseSimpleDTO;
import com.e_commerce.dto.ProductImageResponseDTO;
import com.e_commerce.dto.ProductRequestDTO;
import com.e_commerce.dto.ProductResponseDTO;
import com.e_commerce.model.Category;
import com.e_commerce.model.Product;
import com.e_commerce.model.ProductImage;
import com.e_commerce.repository.ICategoryRepository;
import com.e_commerce.repository.IProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ICategoryRepository categoryRepository;

    @Test
    @DisplayName("GET /api/product - Should return all user for ADMIN")
    @WithMockUser(roles = {"ADMIN"})
    public void getAllProduct_success() throws Exception {

        productRepository.deleteAll();

        Product product1 = Product.builder()
                .name("product1")
                .description("product prueba")
                .price(new BigDecimal("100.00"))
                .stock(10)
                .sku("pru1")
                .imageUrl("www.prueba.com")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        Product product2 = Product.builder()
                .name("product2")
                .description("product prueba2")
                .price(new BigDecimal("100.00"))
                .stock(10)
                .sku("pru2")
                .imageUrl("www.prueba.com")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        productRepository.saveAll(java.util.List.of(product1, product2));

        mockMvc.perform(get("/api/product").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[*].name", hasItem("product1")))
                .andExpect(jsonPath("$[*].description", hasItem("product prueba")))
                .andExpect(jsonPath("$[0].price", is(100.00)))
                .andExpect(jsonPath("$[0].stock", is(10)))
                .andExpect(jsonPath("$[*].sku", hasItem("pru1")))
                .andExpect(jsonPath("$[*].imageUrl", hasItem("www.prueba.com")))
                .andExpect(jsonPath("$[0].isActive", is(true)))
                .andExpect(jsonPath("$[*].createdAt").exists());
    }

    @Test
    @DisplayName("GET /api/product - Should return 403 Forbidden for non-admin GUEST")
    @WithMockUser(roles = {"GUEST"})
    public void getAllProduct_Forbidden() throws Exception {

        mockMvc.perform(get("/api/product").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/product/{id} - Should allow access to ADMIN role")
    @WithMockUser(roles = {"ADMIN"})
    public void getProductById_AdminSuccess() throws Exception {

        Long id = 1L;
        productRepository.deleteAll();
        Product product = Product.builder()
                .name("product")
                .description("product prueba")
                .price(new BigDecimal("100.00"))
                .stock(10)
                .sku("pru1")
                .imageUrl("www.prueba.com")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        Product savedProduct = productRepository.save(product);

        mockMvc.perform(get("/api/product/{id}", savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name", is("product")))
                .andExpect(jsonPath("$.description", is("product prueba")))
                .andExpect(jsonPath("$.price", is(100.00)))
                .andExpect(jsonPath("$.stock", is(10)))
                .andExpect(jsonPath("$.sku", is("pru1")))
                .andExpect(jsonPath("$.imageUrl", is("www.prueba.com")))
                .andExpect(jsonPath("$.isActive", is(true)))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @DisplayName("GET /api/product/{id} - Should return 403 for roles other than USER or ADMIN")
    @WithMockUser(roles = {"GUEST"})
    public void getProductById_ForbiddenForGuest() throws Exception {
        mockMvc.perform(get("/api/product/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/product - Should save a product successfully")
    @WithMockUser(roles = {"ADMIN"})
    public void save_Product() throws Exception {

        categoryRepository.deleteAll();
        Category category = Category.builder()
                .name("electricidad")
                .createdAt(LocalDateTime.now())
                .build();

        Category savedCategory = categoryRepository.save(category);

        productRepository.deleteAll();
        ProductRequestDTO product = ProductRequestDTO.builder()
                .name("product")
                .description("product prueba")
                .price(new BigDecimal("100.00"))
                .stock(10)
                .sku("pru1")
                .categoryId(category.getId())
                .imageUrl("www.prueba.com")
                .isActive(true)
                .build();

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated());

        Product productDB = productRepository.findAll().stream()
                .filter(p -> "pru1".equals(p.getSku()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Product not found."));

        assertThat(productDB.getId()).isNotNull();
        assertThat(productDB.getSku()).isEqualTo("pru1");
    }



}
