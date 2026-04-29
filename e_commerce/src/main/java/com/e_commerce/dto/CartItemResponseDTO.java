package com.e_commerce.dto;

import com.e_commerce.model.Cart;
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
    private CartResponseDTO cart;
    private ProductResponseSimpleDTO product;
    private Integer quantity;

}
