package com.e_commerce.service;


import com.e_commerce.dto.OrderItemRequestDTO;
import com.e_commerce.dto.OrderItemResponseDTO;

import java.util.List;

public interface IOrderItemService {

    public List<OrderItemResponseDTO> findAll();
    public OrderItemResponseDTO findById(Long id);
    public OrderItemResponseDTO save(OrderItemRequestDTO orderItemRequestDTO);
    public void deleteById(Long id);
    public updateById(Long id,OrderItemRequestDTO orderItemRequestDTO);
}
