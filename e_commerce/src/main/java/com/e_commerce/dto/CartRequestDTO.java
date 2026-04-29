package com.e_commerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestDTO {

    @NotBlank(message = "El id. del usuario es obligatorio")
    @Positive(message = "El id. del usuario debe ser un número positivo")
    private Long userId;
    private List<Long> cartItemsIds = new ArrayList<>();
}
