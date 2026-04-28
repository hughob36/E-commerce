package com.e_commerce.service;

import com.e_commerce.dto.CategoryRequestDTO;
import com.e_commerce.dto.CategoryResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.ICategoryMapper;
import com.e_commerce.model.Category;
import com.e_commerce.model.Product;
import com.e_commerce.repository.ICategoryRepository;
import com.e_commerce.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    private final ICategoryRepository categoryRepository;
    private final ICategoryMapper categoryMapper;
    private final IProductRepository productRepository;

    @Override
    public List<CategoryResponseDTO> findAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryMapper.toCategoryResponseDTOList(categoryList);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO findCategoryById(Long id) {
        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category '"+ id +"' not found."));

        //foundCategory.getSubCategories().size();
        //foundCategory.getProducts().size();

       /* // RESALTADO: Paso 1 - Traemos la entidad y cargamos la primera lista (subcategorías)
        Category foundCategory = categoryRepository.findWithSubCategoriesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category '"+ id +"' not found."));

        // RESALTADO: Paso 2 - Ejecutamos la segunda consulta para el mismo ID
        // Hibernate detecta que ya tiene la categoría 'id' en su caché y le "pega" los productos
        categoryRepository.findWithProductsById(id);*/



        return categoryMapper.toCategoryResponseDTO(foundCategory);
    }

    @Override
    public CategoryResponseDTO saveCategory(CategoryRequestDTO categoryRequestDTO) {
        Category categoryRequest = categoryMapper.toCategory(categoryRequestDTO);

        if(categoryRequestDTO.getParentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(categoryRequestDTO.getParentCategoryId())
                    .orElse(null);
            categoryRequest.setParentCategory(parentCategory);
        }

        List<Category> subCategories = categoryRepository.findAllById(categoryRequestDTO.getSubCategoriesIds());
        subCategories.forEach(sub -> sub.setParentCategory(categoryRequest));
        categoryRequest.setSubCategories(subCategories);

        List<Product> productsList = productRepository.findAllById(categoryRequestDTO.getProductIds());
        productsList.forEach(prod -> prod.setCategory(categoryRequest));
        categoryRequest.setProducts(productsList);

        Category saveCategory = categoryRepository.save(categoryRequest);
        return categoryMapper.toCategoryResponseDTO(saveCategory);
    }

    @Override
    public void deleteCategoryById(Long id) {
        if(!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id '" + id + "' not found.");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponseDTO updateCategoryById(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category '"+ id +"' not found."));

        if(categoryRequestDTO.getParentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(categoryRequestDTO.getParentCategoryId())
                    .orElse(null);
            foundCategory.setParentCategory(parentCategory);
        }

        //  Primero, desvinculamos los hijos actuales (les ponemos el padre en null)
        // Esto asegura que si alguno no viene en la nueva lista, pierda la relacion en la DB.
        if (foundCategory.getSubCategories() != null) {
            foundCategory.getSubCategories().forEach(sub -> sub.setParentCategory(null));
            foundCategory.getSubCategories().clear();
        }

        // Ahora traemos los nuevos hijos y les asignamos el padre
        List<Category> newSubCategories = categoryRepository.findAllById(categoryRequestDTO.getSubCategoriesIds());
        newSubCategories.forEach(sub -> sub.setParentCategory(foundCategory));
        foundCategory.getSubCategories().addAll(newSubCategories);

        // MANEJO DE PRODUCTOS (Para permitir eliminar)
        // Lo mismo para los productos
        if (foundCategory.getProducts() != null) {
            foundCategory.getProducts().forEach(prod -> prod.setCategory(null));
            foundCategory.getProducts().clear();
        }

        List<Product> newProductsList = productRepository.findAllById(categoryRequestDTO.getProductIds());
        newProductsList.forEach(prod -> prod.setCategory(foundCategory));
        foundCategory.getProducts().addAll(newProductsList);

        categoryMapper.updateCategoryFromDTO(categoryRequestDTO,foundCategory);
        Category updateCategory = categoryRepository.save(foundCategory);
        return categoryMapper.toCategoryResponseDTO(updateCategory);
    }
}
