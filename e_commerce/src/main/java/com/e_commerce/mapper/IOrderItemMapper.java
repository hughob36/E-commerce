package com.e_commerce.mapper;

import com.e_commerce.dto.OrderItemRequestDTO;
import com.e_commerce.dto.OrderItemResponseDTO;
import com.e_commerce.dto.OrderResponseSimpleDTO;
import com.e_commerce.dto.ProductResponseSimpleDTO;
import com.e_commerce.model.Order;
import com.e_commerce.model.OrderItem;
import com.e_commerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderItemMapper {

    public List<OrderItemResponseDTO> toOrderItemResponseDTOList(List<OrderItem> orderItemList);

    public OrderItemResponseDTO toOrderItemResponseDTO(OrderItem orderItem);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    public OrderItem toOrderItem(OrderItemRequestDTO orderItemRequestDTO);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    public void updateOrderItemFromDTO(OrderItemRequestDTO orderItemRequestDTO, @MappingTarget OrderItem orderItem);

    public OrderResponseSimpleDTO toOrderResponseSimpleDTO(Order order);
    public ProductResponseSimpleDTO toProductResponseSimpleDTO(Product product);
}
