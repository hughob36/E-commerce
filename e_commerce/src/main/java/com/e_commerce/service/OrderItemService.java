package com.e_commerce.service;

import com.e_commerce.dto.OrderItemRequestDTO;
import com.e_commerce.dto.OrderItemResponseDTO;
import com.e_commerce.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService implements IOrderItemService{

    private final IOrderRepository orderRepository;

    @Override
    public List<OrderItemResponseDTO> findAll() {
        return List.of();
    }

    @Override
    public OrderItemResponseDTO findById(Long id) {
        return null;
    }

    @Override
    public OrderItemResponseDTO save(OrderItemRequestDTO orderItemRequestDTO) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public OrderItemResponseDTO updateById(Long id, OrderItemRequestDTO orderItemRequestDTO) {
        return null;
    }
}
