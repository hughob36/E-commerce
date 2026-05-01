package com.e_commerce.mapper;

import com.e_commerce.dto.OrderItemRequestDTO;
import com.e_commerce.dto.OrderItemResponseDTO;
import com.e_commerce.model.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderItemMapper {

    public List<OrderItemResponseDTO> toOrderItemResponseDTOList(List<OrderItem> orderItemList);

    public OrderItemResponseDTO toOrderItemResponseDTO(OrderItem orderItem);

    public OrderItem toOrderItem(OrderItemRequestDTO orderItemRequestDTO);
}
