package com.e_commerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // Producto

    @Column(nullable = false, length = 500)
    private String imageUrl; // URL de la imagen

    @Column(nullable = false)
    private Boolean isPrimary = false; // Es imagen principal

    @Column(name = "image_order")
    private Integer order; // Orden de visualización
}
