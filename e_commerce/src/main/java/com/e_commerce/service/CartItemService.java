package com.e_commerce.service;

import com.e_commerce.dto.CartItemRequestDTO;
import com.e_commerce.dto.CartItemResponseDTO;
import com.e_commerce.repository.ICartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final ICartItemRepository cartItemRepository;

    @Override
    public List<CartItemResponseDTO> getAllCartItem() {


        return List.of();
    }

    @Override
    public CartItemResponseDTO getCartById(Long id) {
        return null;
    }

    @Override
    public CartItemResponseDTO save(CartItemRequestDTO cartItemRequestDTO) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public CartItemResponseDTO updateById(Long id, CartItemRequestDTO cartItemRequestDTO) {
        return null;
    }
}
