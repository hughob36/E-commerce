package com.e_commerce.controller;

import com.e_commerce.dto.OrderItemRequestDTO;
import com.e_commerce.dto.OrderItemResponseDTO;
import com.e_commerce.model.OrderItem;
import com.e_commerce.service.IOrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderItem")
@RequiredArgsConstructor
public class OrderItemController {

    private final IOrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<List<OrderItemResponseDTO>> getAllOrderItem() {
        List<OrderItemResponseDTO> orderItemResponseDTOList = orderItemService.findAll();
        return ResponseEntity.ok(orderItemResponseDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponseDTO> getOrderItemById(@PathVariable Long id) {
        OrderItemResponseDTO orderItemResponseDTO = orderItemService.findById(id);
        return ResponseEntity.ok(orderItemResponseDTO);
    }

    @PostMapping
    public ResponseEntity<OrderItemResponseDTO> createOrderItem(@RequestBody @Valid OrderItemRequestDTO orderItemRequestDTO) {
        OrderItemResponseDTO orderItemResponseDTO = orderItemService.save(orderItemRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderItemResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrderItemById(@PathVariable Long id) {
        orderItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemResponseDTO> updateOrderItemById(@PathVariable Long id,@RequestBody @Valid OrderItemRequestDTO orderItemRequestDTO) {
        OrderItemResponseDTO orderItemResponseDTO = orderItemService.updateById(id,orderItemRequestDTO);
        return ResponseEntity.ok(orderItemResponseDTO);
    }
}
