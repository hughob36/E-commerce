package com.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {

    private Long id;
    private CartResponseSimpleDTO cart;
    private ProductResponseSimpleDTO product;
    private Integer quantity;

}
