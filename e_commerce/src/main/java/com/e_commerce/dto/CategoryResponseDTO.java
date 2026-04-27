package com.e_commerce.dto;

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
    private CategoryResponseSimpleDTO parentCategory;
    private List<CategoryResponseSimpleDTO> subCategories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductResponseSimpleDTO> products;
}
