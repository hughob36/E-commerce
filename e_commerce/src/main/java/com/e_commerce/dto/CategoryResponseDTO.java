package com.e_commerce.dto;

import com.e_commerce.model.Category;
import com.e_commerce.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Category parentCategory;
    private List<Category> subCategories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Product> products;
}
