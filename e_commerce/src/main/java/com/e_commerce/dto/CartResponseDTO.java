package com.e_commerce.dto;

import com.e_commerce.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {

    private Long id;
    private UserAppResponseDTO user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItem> cartItems;
}
