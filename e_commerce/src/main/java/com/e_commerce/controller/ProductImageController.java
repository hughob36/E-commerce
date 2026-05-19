package com.e_commerce.controller;

import com.e_commerce.dto.ProductImageRequestDTO;
import com.e_commerce.dto.ProductImageResponseDTO;
import com.e_commerce.service.IProductImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productImage")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Imágenes de productos", description = "Operaciones relacionadas con imágenes de productos.")
public class ProductImageController {

    private final IProductImageService productImageService;

    @Operation(summary = "Listar imágenes", description = "Obtiene la lista completa de imágenes registradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de imágenes obtenida correctamente.")
    })
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ProductImageResponseDTO>> getAllProductImage() {
        List<ProductImageResponseDTO> productImageResponseDTOList = productImageService.findAllProductImage();
        return ResponseEntity.ok(productImageResponseDTOList);
    }

    @Operation(summary = "Buscar imágen por ID", description = "Devuelve una imágen según su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imágen encontrada."),
            @ApiResponse(responseCode = "404", description = "No se encontró la imágen con el ID indicado.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductImageResponseDTO> getProductImageById(@PathVariable Long id) {
        ProductImageResponseDTO productImageResponseDTO = productImageService.findProductImageById(id);
        return ResponseEntity.ok(productImageResponseDTO);
    }

    @Operation(summary = "Crear una imágen", description = "Registra una nueva imágen de un producto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Imágen creada exitosamente.")
    })
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductImageResponseDTO> createProductImage(@RequestBody @Valid ProductImageRequestDTO productImageRequestDTO) {
        ProductImageResponseDTO productImageResponseDTO = productImageService.saveProductImage(productImageRequestDTO);
        return ResponseEntity.ok(productImageResponseDTO);
    }

    @Operation(summary = "Eliminar imágen", description = "Elimina una imágen existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Imágen eliminada correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró la imágen con el ID indicado.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity deleteProductImageById(@PathVariable Long id) {
        productImageService.deleteProductImageById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar imágen", description = "Modifica los datos de la imágen existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imágen actualizada correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró la imágen con el ID indicado.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductImageResponseDTO> updateProductImageById(@PathVariable Long id,@RequestBody @Valid ProductImageRequestDTO productImageRequestDTO) {
        ProductImageResponseDTO productImageResponseDTO = productImageService.updateProductImageById(id,productImageRequestDTO);
        return ResponseEntity.ok(productImageResponseDTO);
    }
}
