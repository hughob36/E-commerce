package com.e_commerce.dto;

import com.e_commerce.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDTO {

    private Long id;

    private Order order;


    private ProductResponseSimpleDTO product;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
