package com.e_commerce.service;

import com.e_commerce.dto.CartRequestDTO;
import com.e_commerce.dto.CartResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.ICartMapper;
import com.e_commerce.model.Cart;
import com.e_commerce.model.CartItem;
import com.e_commerce.model.UserApp;
import com.e_commerce.repository.ICartItemRepository;
import com.e_commerce.repository.ICartRepository;
import com.e_commerce.repository.IUserAppRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final IUserAppRepository userAppRepository;
    private final ICartItemRepository cartItemRepository;

    @Override
    public List<CartResponseDTO> findAll() {
        List<Cart> cartList = cartRepository.findAll();
        return cartMapper.toCartResponseDTOList(cartList);
    }

    @Override
    public CartResponseDTO findById(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart '"+ id +"' not found."));
        return cartMapper.toCartResponseDTO(cart);
    }

    @Override
    public CartResponseDTO save(CartRequestDTO cartRequestDTO) {
        Cart cart = cartMapper.toCart(cartRequestDTO);
        UserApp user = userAppRepository.findById(cartRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User '"+ cartRequestDTO.getUserId() +"' not found."));
        cart.setUser(user);
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartResponseDTO(savedCart);
    }

    @Override
    public void deleteById(Long id) {
        if(!cartRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id '" + id + "' not found.");
        }
        cartRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CartResponseDTO updateById(Long id, CartRequestDTO cartRequestDTO) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart '"+ id +"' not found."));

        UserApp user = userAppRepository.findById(cartRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User '"+ cartRequestDTO.getUserId() +"' not found."));
        cart.setUser(user);

        cart.getCartItems().clear();
        List<CartItem> cartItemList = cartItemRepository.findAllById(cartRequestDTO.getCartItemsIds());
        cartItemList.forEach(cartItem -> cartItem.setCart(cart));
        cart.getCartItems().addAll(cartItemList);

        Cart updateCart = cartRepository.save(cart);

        return cartMapper.toCartResponseDTO(updateCart);
    }
}
