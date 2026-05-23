package com.e_commerce.service;

import com.e_commerce.dto.OrderRequestDTO;
import com.e_commerce.dto.OrderResponseDTO;
import com.e_commerce.exception.InsufficientStockException;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.IOrderMapper;
import com.e_commerce.model.*;
import com.e_commerce.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

   private final IOrderRepository orderRepository;
   private final IOrderMapper orderMapper;
   private final IUserAppRepository userAppRepository;
   private final IOrderItemRepository orderItemRepository;
   private final IProductRepository productRepository;
   private final ICartRepository cartRepository;
   private final ICartItemRepository cartItemRepository;

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
    @Transactional
    public OrderResponseDTO save(OrderRequestDTO orderRequestDTO) {

        UserApp user = userAppRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with Id '" + orderRequestDTO.getUserId() + "' not found."));

        // 2. Buscar el carrito del usuario primero (Lo necesitamos para obtener los productos a comprar)
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user with Id '" + user.getId() + "'."));

        // Obtener los items reales que el usuario tiene en el carrito
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems.isEmpty()) {
            throw new InsufficientStockException("Cannot create an order with an empty cart.");
        }

        // 3. Mapear DTO inicial a la entidad Orden
        Order order = orderMapper.toOrder(orderRequestDTO);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        // 4. Transformar CartItems en OrderItems y procesar Stock simultáneamente
        List<OrderItem> orderItemList = cartItems.stream().map(cartItem -> {
            // Bloqueo pesimista por cada producto del carrito
            Product product = productRepository.findByIdWithLock(cartItem.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product with Id '" + cartItem.getProduct().getId() + "' not found."));

            // Validar Stock
            if (product.getStock() < cartItem.getQuantity()) {
                throw new InsufficientStockException("Sorry, the product '" + product.getName() + "' run out of stock.");
            }

            // Restar Stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            // Crear el OrderItem a partir del CartItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(product.getPrice());

            // Convertimos Integer a BigDecimal y multiplicamos usando .multiply()
            BigDecimal quantityBigDecimal = BigDecimal.valueOf(cartItem.getQuantity());
            BigDecimal calculatedSubtotal = quantityBigDecimal.multiply(product.getPrice());

            orderItem.setSubtotal(calculatedSubtotal);// Es vital clavar el precio del momento de la compra
            orderItem.setOrder(order);

            return orderItem;
        }).toList();

        order.setOrderItems(orderItemList);

        // 5. Guardar la orden y sus items (gracias al CascadeType.ALL en Order)
        Order savedOrder = orderRepository.save(order);

        // 6. Vaciar los items del carrito de manera eficiente
        cartItemRepository.deleteByCartId(cart.getId());

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
        Order foundOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order '"+ id +"' not found."));

        UserApp user = userAppRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User '"+ orderRequestDTO.getUserId() +"' not found."));
        foundOrder.setUser(user);

        List<OrderItem> orderItemList = orderItemRepository.findAllById(orderRequestDTO.getOrderItemsIds());
        orderItemList.forEach(item -> item.setOrder(foundOrder));
        foundOrder.setOrderItems(orderItemList);

        orderMapper.updateOrderFromDTO(orderRequestDTO,foundOrder);

        Order orderUpdate = orderRepository.save(foundOrder);
        return orderMapper.toOrderResponseDTO(orderUpdate);
    }
}
