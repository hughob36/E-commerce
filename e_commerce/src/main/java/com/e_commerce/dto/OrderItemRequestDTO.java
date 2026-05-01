package com.e_commerce.dto;

import com.e_commerce.model.Order;
import com.e_commerce.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequestDTO {


    private Long orderId;

    private Long productId;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal subtotal;
}
