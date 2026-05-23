package com.e_commerce.service;

import com.e_commerce.dto.CartItemRequestDTO;
import com.e_commerce.dto.CartItemResponseDTO;
import com.e_commerce.exception.InsufficientStockException;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.ICartItemMapper;
import com.e_commerce.model.Cart;
import com.e_commerce.model.CartItem;
import com.e_commerce.model.Product;
import com.e_commerce.repository.ICartItemRepository;
import com.e_commerce.repository.ICartRepository;
import com.e_commerce.repository.IProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
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
    @Transactional
    public CartItemResponseDTO save(CartItemRequestDTO cartItemRequestDTO) {

        Cart cart = cartRepository.findById(cartItemRequestDTO.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart with Id '"+ cartItemRequestDTO.getCartId() +"' not found."));

        Product product = productRepository.findById(cartItemRequestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product with Id '"+ cartItemRequestDTO.getProductId() +"' not found."));

        // 2. Controlar si el producto ya está en el carrito para actualizar o crear
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(new CartItem()); // Si no existe, creamos uno nuevo

        int newQuantity = cartItemRequestDTO.getQuantity();

        if (cartItem.getId() != null) {
            // Si ya existía, sumamos la cantidad que ya tenía en el carrito
            newQuantity += cartItem.getQuantity();
        }

        // 3. Validar Stock con la semántica correcta (Error 400 en vez de 404)
        if (newQuantity > product.getStock()) {
            throw new InsufficientStockException("Not enough stock available. Requested: " + newQuantity + ", Available: " + product.getStock());
        }

        // 4. Seteas los datos finales
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
        }
        cartItem.setQuantity(newQuantity);

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
