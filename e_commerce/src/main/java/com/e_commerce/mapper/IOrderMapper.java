package com.e_commerce.mapper;

import com.e_commerce.dto.OrderRequestDTO;
import com.e_commerce.dto.OrderResponseDTO;
import com.e_commerce.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderMapper {

    public List<OrderResponseDTO> toOrderesponseDTOList(List<Order> orderList);

    public OrderResponseDTO toOrderResponseDTO(Order order);

    public Order toOrder(OrderRequestDTO orderRequestDTO);

    public void updateOrderResponseDTOFromDTO(OrderRequestDTO orderRequestDTO, @MappingTarget Order order);
}
