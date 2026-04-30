package com.e_commerce.service;

import com.e_commerce.dto.CartItemRequestDTO;
import com.e_commerce.dto.CartItemResponseDTO;

import java.util.List;

public interface ICartItemService {

    public List<CartItemResponseDTO> getAllCartItem();
    public CartItemResponseDTO getCartById(Long id);
    public CartItemResponseDTO save(CartItemRequestDTO cartItemRequestDTO);
    public void deleteById(Long id);
    public CartItemResponseDTO updateById(Long id, CartItemRequestDTO cartItemRequestDTO);
}
