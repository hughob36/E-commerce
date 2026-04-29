package com.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequestDTO {

    private Long cartId;
    private Long productId;
    private Integer quantity;
}
