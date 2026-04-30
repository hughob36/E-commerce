package com.e_commerce.mapper;

import com.e_commerce.dto.CartRequestDTO;
import com.e_commerce.dto.CartResponseDTO;
import com.e_commerce.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICartMapper {

    public List<CartResponseDTO> toCartResponseDTOList(List<Cart> cartList);
    public CartResponseDTO toCartResponseDTO(Cart cart);

    @Mapping(target = "user", ignore = true)
    public Cart toCart(CartRequestDTO cartRequestDTO);

}
