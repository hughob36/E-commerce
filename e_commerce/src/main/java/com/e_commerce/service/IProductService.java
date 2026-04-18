package com.e_commerce.service;


import com.e_commerce.dto.ProductRequestDTO;
import com.e_commerce.dto.ProductResponseDTO;

import java.util.List;

public interface IProductService {

    public List<ProductResponseDTO> findAll();
    public ProductResponseDTO findById(Long id);
    public ProductResponseDTO save(ProductRequestDTO productRequestDTO);
    public void deleteById(Long id);
    public ProductResponseDTO update(Long id, ProductRequestDTO productRequestDTO);
}
