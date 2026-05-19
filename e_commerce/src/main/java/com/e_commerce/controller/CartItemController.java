package com.e_commerce.controller;

import com.e_commerce.dto.CartItemRequestDTO;
import com.e_commerce.dto.CartItemResponseDTO;
import com.e_commerce.service.ICartItemService;
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
@RequestMapping("/api/cartItem")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Carrito items", description = "Operaciones relacionadas con items del carrito.")
public class CartItemController {

    private final ICartItemService cartItemService;

    @Operation(summary = "Listar items del carrito", description = "Obtiene la lista completa de items del carrito.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de items del carrito obtenida correctamente.")
    })
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<CartItemResponseDTO>> getAllCartItem() {
        List<CartItemResponseDTO> cartItemResponseDTOList = cartItemService.findAll();
        return ResponseEntity.ok(cartItemResponseDTOList);
    }

    @Operation(summary = "Buscar items de carrito por ID", description = "Devuelve un items del carrito según su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Items encontrado."),
            @ApiResponse(responseCode = "404", description = "No se encontró un items con el ID indicado.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CartItemResponseDTO> getCartItemById(@PathVariable Long id) {
        CartItemResponseDTO cartItemResponseDTO = cartItemService.findById(id);
        return ResponseEntity.ok(cartItemResponseDTO);
    }

    @Operation(summary = "Crear un items de carrito", description = "Registra un nuevo items en carrito.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Items creado exitosamente.")
    })
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CartItemResponseDTO> createCartItem(@RequestBody @Valid CartItemRequestDTO cartItemRequestDTO) {
        CartItemResponseDTO cartItemResponseDTO = cartItemService.save(cartItemRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemResponseDTO);
    }

    @Operation(summary = "Eliminar items del carrito", description = "Elimina un items existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Items eliminado correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró un items con el ID indicado.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity deleteCartItemById(@PathVariable Long id) {
        cartItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar items", description = "Modifica los datos de un items del carrito existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Items actualizado correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró un items con el ID indicado.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CartItemResponseDTO> updateCartItemById(@PathVariable Long id,@RequestBody @Valid CartItemRequestDTO cartItemRequestDTO) {
        CartItemResponseDTO cartItemResponseDTO = cartItemService.updateById(id,cartItemRequestDTO);
        return ResponseEntity.ok(cartItemResponseDTO);
    }
}
