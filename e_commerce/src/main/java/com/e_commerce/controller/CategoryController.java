package com.e_commerce.controller;

import com.e_commerce.dto.CategoryRequestDTO;
import com.e_commerce.dto.CategoryResponseDTO;
import com.e_commerce.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Categoria", description = "Operaciones relacionadas con la categoria de los productos.")
public class CategoryController {

    private final ICategoryService categoryService;

    @Operation(summary = "Listar categorias", description = "Obtiene la lista completa de categorias registradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de categorias obtenida correctamente.")
    })
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategory() {
        List<CategoryResponseDTO> categoryResponseDTOList = categoryService.findAllCategory();
        return ResponseEntity.ok(categoryResponseDTOList);
    }

    @Operation(summary = "Buscar categoria por ID", description = "Devuelve una categoria según su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada."),
            @ApiResponse(responseCode = "404", description = "No se encontró una categoria con el ID indicado.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        CategoryResponseDTO categoryResponseDTO = categoryService.findCategoryById(id);
        return ResponseEntity.ok(categoryResponseDTO);
    }

    @Operation(summary = "Crear una categoria", description = "Registra una nueva categoria.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria creada exitosamente."),
            @ApiResponse(responseCode = "409", description = "Categoria ya existe.")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody @Valid CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.saveCategory(categoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponseDTO);
    }

    @Operation(summary = "Eliminar categoria", description = "Elimina una categoria existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria eliminada correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró una categoria con el ID indicado.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar categoria", description = "Modifica los datos de una categoria existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria actualizada correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró una categoria con el ID indicado."),
            @ApiResponse(responseCode = "409", description = "Categoria ya existe.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> updateCategoryById(@PathVariable Long id, @RequestBody @Valid CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.updateCategoryById(id,categoryRequestDTO);
        return ResponseEntity.ok(categoryResponseDTO);
    }

}
