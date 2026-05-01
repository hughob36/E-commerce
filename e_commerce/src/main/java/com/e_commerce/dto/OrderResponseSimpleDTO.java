package com.e_commerce.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseSimpleDTO {

    private Long id;
    private UserAppResponseSimpleDTO user;
    private List<OrderItemResponseSimpleDTO> orderItems;
}
