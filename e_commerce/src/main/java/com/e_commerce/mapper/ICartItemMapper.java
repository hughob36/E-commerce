package com.e_commerce.mapper;

import com.e_commerce.dto.CartItemRequestDTO;
import com.e_commerce.dto.CartItemResponseDTO;
import com.e_commerce.dto.CartResponseSimpleDTO;
import com.e_commerce.dto.ProductResponseSimpleDTO;
import com.e_commerce.model.Cart;
import com.e_commerce.model.CartItem;
import com.e_commerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICartItemMapper {

    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", ignore = true)
    public CartItem toCartItem(CartItemRequestDTO cartItemRequestDTO);

    public List<CartItemResponseDTO> toCartItemResponseDTOList(List<CartItem> cartItemList);

    public CartItemResponseDTO toCartItemresponseDTO(CartItem cartItem);

    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", ignore = true)
    public void updateCartItemFromDTO(CartItemRequestDTO cartItemRequestDTO, @MappingTarget CartItem cartItem);

    public CartResponseSimpleDTO toCartResponseSimpleDTO(Cart cart);
    public ProductResponseSimpleDTO toProductResponseSimpleDTO(Product product);
}
