package com.e_commerce.mapper;

import com.e_commerce.dto.OrderItemResponseSimpleDTO;
import com.e_commerce.dto.OrderRequestDTO;
import com.e_commerce.dto.OrderResponseDTO;
import com.e_commerce.model.Order;
import com.e_commerce.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderMapper {

    public List<OrderResponseDTO> toOrderesponseDTOList(List<Order> orderList);

    @Mapping(target = "orderItemsIds", source = "orderItems")
    public OrderResponseDTO toOrderResponseDTO(Order order);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    public Order toOrder(OrderRequestDTO orderRequestDTO);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    public void updateOrderFromDTO(OrderRequestDTO orderRequestDTO, @MappingTarget Order order);


    public OrderItemResponseSimpleDTO toOrderItemResponseSimpleDTO(OrderItem orderItems);
}
