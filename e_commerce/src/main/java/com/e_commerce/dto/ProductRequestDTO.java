package com.e_commerce.dto;

import com.e_commerce.model.Category;
import com.e_commerce.model.ProductImage;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import java.util.List;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String name;

    @Size(max = 2000, message = "La descripción es demasiado larga")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "Formato de precio inválido (máximo 10 enteros y 2 decimales)")
    private BigDecimal price;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotBlank(message = "El SKU es obligatorio")
    @Size(max = 100, message = "El SKU no puede exceder los 100 caracteres")
    private String sku;

    @NotNull(message = "La categoría es obligatoria")
    private Category category;

    @Size(max = 500, message = "La URL de la imagen es demasiado larga")
    private String imageUrl;

    private Boolean isActive = true;

    private List<ProductImage> images; // Lista de URLs para las imágenes adicionale
}
