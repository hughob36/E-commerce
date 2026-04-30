package com.e_commerce.service;

import com.e_commerce.dto.CartItemRequestDTO;
import com.e_commerce.dto.CartItemResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.ICartItemMapper;
import com.e_commerce.model.Cart;
import com.e_commerce.model.CartItem;
import com.e_commerce.model.Product;
import com.e_commerce.repository.ICartItemRepository;
import com.e_commerce.repository.ICartRepository;
import com.e_commerce.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final ICartItemRepository cartItemRepository;
    private final ICartItemMapper cartItemMapper;
    private final ICartRepository cartRepository;
    private final IProductRepository productRepository;

    @Override
    public List<CartItemResponseDTO> findAll() {
        List<CartItem> cartItemList = cartItemRepository.findAll();
        return cartItemMapper.toCartItemResponseDTOList(cartItemList);
    }

    @Override
    public CartItemResponseDTO findById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id '"+ id +"' not found."));
        return cartItemMapper.toCartItemresponseDTO(cartItem);
    }

    @Override
    public CartItemResponseDTO save(CartItemRequestDTO cartItemRequestDTO) {

        CartItem cartItem = cartItemMapper.toCartItem(cartItemRequestDTO);

        Cart cart = cartRepository.findById(cartItemRequestDTO.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Id '"+ cartItemRequestDTO.getCartId() +"' not found."));
        cartItem.setCart(cart);

        Product product = productRepository.findById(cartItemRequestDTO.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Id '"+ cartItemRequestDTO.getProductId() +"' not found."));
        cartItem.setProduct(product);

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toCartItemresponseDTO(savedCartItem);
    }

    @Override
    public void deleteById(Long id) {
        if(!cartItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id '"+ id +"' not found.");
        }
        cartItemRepository.deleteById(id);
    }

    @Override
    public CartItemResponseDTO updateById(Long id, CartItemRequestDTO cartItemRequestDTO) {

        CartItem foundCartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id '"+ id +"' not found."));

        Cart cart = cartRepository.findById(cartItemRequestDTO.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Id '"+ cartItemRequestDTO.getCartId() +"' not found."));
        foundCartItem.setCart(cart);

        Product product = productRepository.findById(cartItemRequestDTO.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Id '"+ cartItemRequestDTO.getProductId() +"' not found."));
        foundCartItem.setProduct(product);

        cartItemMapper.updateCartItemFromDTO(cartItemRequestDTO,foundCartItem);

        CartItem updateCartItem = cartItemRepository.save(foundCartItem);

        return cartItemMapper.toCartItemresponseDTO(updateCartItem);
    }
}
