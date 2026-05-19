package com.e_commerce.controller;

import com.e_commerce.dto.ProductRequestDTO;
import com.e_commerce.dto.ProductResponseDTO;
import com.e_commerce.service.IProductService;
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
@RequestMapping("/api/product")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Productos", description = "Operaciones relacionadas con la gestión de productos.")
public class ProductController {

    private final IProductService productService;

    @Operation(summary = "Listar productos", description = "Obtiene la lista completa de productos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente.")
    })
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponseDTO>> getAllProduct() {
        List<ProductResponseDTO> productResponseDTOList = productService.findAll();
        return ResponseEntity.ok(productResponseDTOList);
    }

    @Operation(summary = "Buscar producto por ID", description = "Devuelve un producto según su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado."),
            @ApiResponse(responseCode = "404", description = "No se encontró el producto con el ID indicado.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO productResponseDTO = productService.findById(id);
        return ResponseEntity.ok(productResponseDTO);
    }

    @Operation(summary = "Crear un producto", description = "Registra un nuevo producto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente.")
    })
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO) {
        ProductResponseDTO saveProductResponseDTO = productService.save(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveProductResponseDTO);
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró el producto con el ID indicado.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity deleteProductById(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Actualizar producto", description = "Modifica los datos de un producto existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró el producto con el ID indicado.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> updateProductById(@PathVariable Long id, @RequestBody @Valid ProductRequestDTO productRequestDTO) {
        ProductResponseDTO productResponseDTO = productService.update(id,productRequestDTO);
        return ResponseEntity.ok(productResponseDTO);
    }
}
