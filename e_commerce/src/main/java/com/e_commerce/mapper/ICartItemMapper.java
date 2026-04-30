package com.e_commerce.mapper;

import com.e_commerce.dto.CartItemRequestDTO;
import com.e_commerce.dto.CartItemResponseDTO;
import com.e_commerce.model.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICartItemMapper {

    public CartItem toCartItem(CartItemRequestDTO cartItemRequestDTO);

    public List<CartItemResponseDTO> toCartItemResponseDTOList(List<CartItem> cartItemList);

    public CartItemResponseDTO toCartItemresponseDTO(CartItem cartItem);
}
