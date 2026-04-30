package com.e_commerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequestDTO {

    @NotNull(message = "El id. del carrito es obligatorio")
    @Positive(message = "El id. del carrito debe ser un número positivo")
    private Long cartId;

    @NotNull(message = "El id. del producto es obligatorio")
    @Positive(message = "El id. del producto debe ser un número positivo")
    private Long productId;
    private Integer quantity;
}
