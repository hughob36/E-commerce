package com.e_commerce.service;

import com.e_commerce.dto.CartRequestDTO;
import com.e_commerce.dto.CartResponseDTO;
import com.e_commerce.repository.ICartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final ICartRepository cartRepository;

    @Override
    public List<CartResponseDTO> findAll() {
        return List.of();
    }

    @Override
    public CartResponseDTO findById(Long id) {
        return null;
    }

    @Override
    public CartResponseDTO save(CartRequestDTO cartRequestDTO) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public CartResponseDTO updateById(Long id, CartRequestDTO cartRequestDTO) {
        return null;
    }
}
