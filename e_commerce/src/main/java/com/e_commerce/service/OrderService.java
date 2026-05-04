package com.e_commerce.service;

import com.e_commerce.dto.OrderRequestDTO;
import com.e_commerce.dto.OrderResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.IOrderMapper;
import com.e_commerce.model.Order;
import com.e_commerce.model.OrderItem;
import com.e_commerce.model.UserApp;
import com.e_commerce.repository.IOrderItemRepository;
import com.e_commerce.repository.IOrderRepository;
import com.e_commerce.repository.IUserAppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

   private final IOrderRepository orderRepository;
   private final IOrderMapper orderMapper;
   private final IUserAppRepository userAppRepository;
   private final IOrderItemRepository orderItemRepository;

    @Override
    public List<OrderResponseDTO> findAll() {
        List<Order> orderList = orderRepository.findAll();
        return orderMapper.toOrderesponseDTOList(orderList);
    }

    @Override
    public OrderResponseDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id '"+ id +"' not found."));
        return orderMapper.toOrderResponseDTO(order);
    }

    @Override
    public OrderResponseDTO save(OrderRequestDTO orderRequestDTO) {

       Order order = orderMapper.toOrder(orderRequestDTO);

        UserApp user = userAppRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User '"+ orderRequestDTO.getUserId() +"' not found."));
        order.setUser(user);

        List<OrderItem> orderItemList = orderItemRepository.findAllById(orderRequestDTO.getOrderItemsIds());
        orderItemList.forEach(item -> item.setOrder(order));
        order.setOrderItems(orderItemList);

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponseDTO(savedOrder);
    }

    @Override
    public void deleteById(Long id) {
        if(!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id '"+ id +"' not found.");
        }
        orderRepository.deleteById(id);
    }

    @Override
    public OrderResponseDTO updateById(Long id, OrderRequestDTO orderRequestDTO) {
        return null;
    }
}
