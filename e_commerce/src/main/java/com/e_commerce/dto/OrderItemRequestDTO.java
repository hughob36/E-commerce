package com.e_commerce.dto;

import com.e_commerce.model.Order;
import com.e_commerce.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequestDTO {

    @NotNull(message = "El id. de la orden es obligatoria")
    @Positive(message = "El id. de la orden debe ser un número positivo")
    private Long orderId;

    @NotNull(message = "El id. del producto es obligatorio")
    @Positive(message = "El id. del producto debe ser un número positivo")
    private Long productId;

    @NotNull(message = "La cantidad del producto es obligatoria")
    private Integer quantity;
    @NotNull(message = "El precio unitario del producto es obligatorio")
    private BigDecimal unitPrice;
    @NotNull(message = "El subtotal del producto es obligatorio")
    private BigDecimal subtotal;
}
