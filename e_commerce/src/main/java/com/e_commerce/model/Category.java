package com.e_commerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID

    @Column(nullable = false, length = 100)
    private String name; // Nombre

    @Column(columnDefinition = "TEXT")
    private String description; // Descripción

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory; // Categoría padre (para subcategorías)

    @OneToMany(mappedBy = "parentCategory")
    private List<Category> subCategories; // Subcategorías

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // Fecha de creación

    @LastModifiedDate
    private LocalDateTime updatedAt; // Fecha de actualización

    @OneToMany(mappedBy = "category")
    private List<Product> products; // Productos de esta categoría

}
