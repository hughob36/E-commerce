package com.e_commerce.mapper;

import com.e_commerce.dto.CartItemResponseSimpleDTO;
import com.e_commerce.dto.CartRequestDTO;
import com.e_commerce.dto.CartResponseDTO;
import com.e_commerce.model.Cart;
import com.e_commerce.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICartMapper {

    public List<CartResponseDTO> toCartResponseDTOList(List<Cart> cartList);
    public CartResponseDTO toCartResponseDTO(Cart cart);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    public Cart toCart(CartRequestDTO cartRequestDTO);

    @Mapping(target = "cartId", source = "cart.id")
    public CartItemResponseSimpleDTO toCartItemResponseDTO(CartItem cartItem);

}
