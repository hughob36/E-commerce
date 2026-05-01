package com.e_commerce.dto;

import com.e_commerce.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {

    @NotNull(message = "El id. del usuario es obligatorio")
    @Positive(message = "El id. del usuario debe ser un número positivo")
    private Long userId;


    private BigDecimal totalAmount;


    private OrderStatus status; // Estado (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)


    private String shippingAddress;


    private String shippingCity;


    private String shippingPostalCode;


    private String shippingPhone;


    private List<Long> orderItemsIds;
}
