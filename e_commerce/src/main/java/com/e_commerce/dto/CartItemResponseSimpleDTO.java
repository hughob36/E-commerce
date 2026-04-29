package com.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseSimpleDTO {

    private Long id;
    private Long cartId;
    private ProductResponseSimpleDTO product;
    private Integer quantity;
}
