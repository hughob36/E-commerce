package com.e_commerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart; // Carrito

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // Producto

    @Column(nullable = false)
    private Integer quantity; // Cantidad

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime addedAt; // Fecha de agregado

}
