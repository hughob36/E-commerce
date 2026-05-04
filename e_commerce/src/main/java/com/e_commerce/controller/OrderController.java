package com.e_commerce.controller;

import com.e_commerce.dto.OrderRequestDTO;
import com.e_commerce.dto.OrderResponseDTO;
import com.e_commerce.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrder() {
        List<OrderResponseDTO> orderResponseDTOList = orderService.findAll();
        return ResponseEntity.ok(orderResponseDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        OrderResponseDTO orderResponseDTO = orderService.findById(id);
        return ResponseEntity.ok(orderResponseDTO);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO orderResponseDTO = orderService.save(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrderById(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrderById(@PathVariable Long id, @RequestBody @Valid OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO orderResponseDTO = orderService.updateById(id,orderRequestDTO);
        return ResponseEntity.ok(orderResponseDTO);
    }
}
