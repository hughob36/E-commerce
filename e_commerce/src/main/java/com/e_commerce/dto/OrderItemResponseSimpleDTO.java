package com.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseSimpleDTO {

    private Long id;
    private OrderResponseSimpleDTO order;
    private ProductResponseSimpleDTO product;
}
