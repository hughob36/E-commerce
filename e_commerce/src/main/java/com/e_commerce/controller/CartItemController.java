package com.e_commerce.controller;

import com.e_commerce.dto.CartItemRequestDTO;
import com.e_commerce.dto.CartItemResponseDTO;
import com.e_commerce.service.ICartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartItem")
@RequiredArgsConstructor
public class CartItemController {

    private final ICartItemService cartItemService;

    @GetMapping
    public ResponseEntity<List<CartItemResponseDTO>> getAllCartItem() {
        List<CartItemResponseDTO> cartItemResponseDTOList = cartItemService.findAll();
        return ResponseEntity.ok(cartItemResponseDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItemResponseDTO> getCartItemById(@PathVariable Long id) {
        CartItemResponseDTO cartItemResponseDTO = cartItemService.findById(id);
        return ResponseEntity.ok(cartItemResponseDTO);
    }

    @PostMapping
    public ResponseEntity<CartItemResponseDTO> createCartItem(@RequestBody @Valid CartItemRequestDTO cartItemRequestDTO) {
        CartItemResponseDTO cartItemResponseDTO = cartItemService.save(cartItemRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCartItemById(@PathVariable Long id) {
        cartItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemResponseDTO> updateCartItemById(@PathVariable Long id,@RequestBody @Valid CartItemRequestDTO cartItemRequestDTO) {
        CartItemResponseDTO cartItemResponseDTO = cartItemService.updateById(id,cartItemRequestDTO);
        return ResponseEntity.ok(cartItemResponseDTO);
    }
}
