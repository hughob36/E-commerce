package com.e_commerce.controller;

import com.e_commerce.dto.OrderItemResponseDTO;
import com.e_commerce.model.OrderItem;
import com.e_commerce.service.IOrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orderItem")
@RequiredArgsConstructor
public class OrderItemController {

    private IOrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<List<OrderItemResponseDTO>> getAllOrderItem() {
        List<OrderItemResponseDTO> orderItemResponseDTOList = orderItemService.findAll();
        return ResponseEntity.ok(orderItemResponseDTOList);
    }


}
