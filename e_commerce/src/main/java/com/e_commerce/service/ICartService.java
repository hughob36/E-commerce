package com.e_commerce.service;

import com.e_commerce.dto.CartRequestDTO;
import com.e_commerce.dto.CartResponseDTO;

import java.util.List;

public interface ICartService {

    public List<CartResponseDTO> findAll();
    public CartResponseDTO findById(Long id);
    public CartResponseDTO save(CartRequestDTO cartRequestDTO);
    public void deleteById(Long id);
    public CartResponseDTO updateById(Long id, CartRequestDTO cartRequestDTO);
}
