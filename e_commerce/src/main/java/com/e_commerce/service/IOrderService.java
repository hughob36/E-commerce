package com.e_commerce.service;

import com.e_commerce.dto.OrderRequestDTO;
import com.e_commerce.dto.OrderResponseDTO;

import java.util.List;

public interface IOrderService {

    public List<OrderResponseDTO> findAll();
    public OrderResponseDTO findById(Long id);
    public OrderResponseDTO save(OrderRequestDTO orderRequestDTO);
    public void deleteById(Long id);
    public OrderResponseDTO updateById(Long id,OrderRequestDTO orderRequestDTO);
}
