package com.e_commerce.service;

import com.e_commerce.dto.OrderItemRequestDTO;
import com.e_commerce.dto.OrderItemResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.IOrderItemMapper;
import com.e_commerce.model.Order;
import com.e_commerce.model.OrderItem;
import com.e_commerce.model.Product;
import com.e_commerce.repository.IOrderItemRepository;
import com.e_commerce.repository.IOrderRepository;
import com.e_commerce.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService implements IOrderItemService{

    private final IOrderItemRepository orderItemRepository;
    private final IOrderItemMapper orderItemMapper;
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;

    @Override
    public List<OrderItemResponseDTO> findAll() {
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        return orderItemMapper.toOrderItemResponseDTOList(orderItemList);
    }

    @Override
    public OrderItemResponseDTO findById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem '"+ id +"' not found."));
        return orderItemMapper.toOrderItemResponseDTO(orderItem);
    }

    @Override
    public OrderItemResponseDTO save(OrderItemRequestDTO orderItemRequestDTO) {

        OrderItem orderItem = orderItemMapper.toOrderItem(orderItemRequestDTO);

        Order foundOrder = orderRepository.findById(orderItemRequestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order '"+ orderItemRequestDTO.getOrderId() +"' not found."));
        orderItem.setOrder(foundOrder);

        Product foundProduct = productRepository.findById(orderItemRequestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product '"+ orderItemRequestDTO.getProductId() +"' not found."));
        orderItem.setProduct(foundProduct);

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        return orderItemMapper.toOrderItemResponseDTO(savedOrderItem);
    }

    @Override
    public void deleteById(Long id) {
        if(!orderItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("OrderItem '"+ id +"' not found.");
        }
        orderItemRepository.deleteById(id);
    }

    @Override
    public OrderItemResponseDTO updateById(Long id, OrderItemRequestDTO orderItemRequestDTO) {

        return null;
    }
}
