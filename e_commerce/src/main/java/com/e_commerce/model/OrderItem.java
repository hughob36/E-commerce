package com.e_commerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity; // Cantidad

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice; // Precio unitario (al momento de la compra)

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal; // Subtotal (quantity * unitPrice)
}
