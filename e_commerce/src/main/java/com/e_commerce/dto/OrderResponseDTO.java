package com.e_commerce.dto;

import com.e_commerce.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    private Long id;
    private UserAppResponseSimpleDTO user;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String shippingAddress;
    private String shippingCity;
    private String shippingPostalCode;
    private String shippingPhone;


    private List<Long> orderItemsIds;
}
