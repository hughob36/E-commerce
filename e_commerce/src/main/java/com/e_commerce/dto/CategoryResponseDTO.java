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

    //private Category parentCategory;

    private Long parentCategoyId;
    private String parentCategoryName;

    // Esto permite ver el árbol hacia ABAJO (hijos), pero no hacia ARRIBA (padre)
    private List<CategoryResponseDTO> subCategories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Opcional: Solo si realmente necesitas los productos al listar categorías
    //private List<ProductResponseDTO> products;
}
