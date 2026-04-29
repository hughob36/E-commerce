package com.e_commerce.controller;

import com.e_commerce.dto.CartRequestDTO;
import com.e_commerce.dto.CartResponseDTO;
import com.e_commerce.service.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;

    @GetMapping
    public ResponseEntity<List<CartResponseDTO>> getAllCart() {
        List<CartResponseDTO> cartResponseDTOList = cartService.findAll();
        return ResponseEntity.ok(cartResponseDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponseDTO> getCartById(@PathVariable @Valid Long id) {
        CartResponseDTO cartResponseDTO = cartService.findById(id);
        return ResponseEntity.ok(cartResponseDTO);
    }

    @PostMapping
    public ResponseEntity<CartResponseDTO> createCart(@RequestBody CartRequestDTO cartRequestDTO) {
        CartResponseDTO cartResponseDTO = cartService.save(cartRequestDTO);
        return ResponseEntity.ok(cartResponseDTO);
    }
}
