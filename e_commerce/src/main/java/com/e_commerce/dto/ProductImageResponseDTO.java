package com.e_commerce.dto;

import com.e_commerce.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageResponseDTO {

    private Long id;
    private Product product;
    private String imageUrl;
    private Boolean isPrimary = false;
    private Integer order;
}
