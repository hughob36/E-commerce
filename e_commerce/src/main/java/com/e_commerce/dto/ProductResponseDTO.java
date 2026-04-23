package com.e_commerce.dto;

import com.e_commerce.model.Category;
import com.e_commerce.model.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String sku;

    // Usamos el DTO, pero cuidado: si CategoryResponseDTO tiene lista de productos,
    // podrías crear recursividad otra vez.
    // Sugerencia: Crea un CategorySimpleDTO (solo id y nombre) para el producto.
    private CategoryResponseSimpleDTO category;


    private String imageUrl;
    private Boolean isActive = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductImageResponseDTO> images;
}
