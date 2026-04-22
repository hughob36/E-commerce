package com.e_commerce.dto;

import com.e_commerce.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageRequestDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Image URL cannot be blank")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    private Boolean isPrimary = false;
    private Integer order;
}
