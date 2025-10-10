package com.e_commerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserApp user; // Usuario

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount; // Monto total

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status; // Estado (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)

    @Column(nullable = false, length = 255)
    private String shippingAddress; // Dirección de envío

    @Column(nullable = false, length = 100)
    private String shippingCity; // Ciudad de envío

    @Column(length = 20)
    private String shippingPostalCode; // Código postal

    @Column(length = 20)
    private String shippingPhone; // Teléfono de contacto

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // Fecha de creación

    @LastModifiedDate
    private LocalDateTime updatedAt; // Fecha de actualización

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems; // Items del pedido

}
