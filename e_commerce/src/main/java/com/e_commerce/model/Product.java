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
@Table(name = "Products")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID

    @Column(nullable = false, length = 255)
    private String name; // Nombre

    @Column(columnDefinition = "TEXT")
    private String description; // Descripción

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // Precio

    @Column(nullable = false)
    private Integer stock; // Stock/Inventario

    @Column(unique = true, length = 100)
    private String sku; // Código único del producto

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // Categoría

    @Column(length = 500)
    private String imageUrl; // URL de imagen

    @Column(nullable = false)
    private Boolean isActive = true; // Está activo

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // Fecha de creación

    @LastModifiedDate
    private LocalDateTime updatedAt; // Fecha de actualización

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> images; // Imágenes del producto
}
