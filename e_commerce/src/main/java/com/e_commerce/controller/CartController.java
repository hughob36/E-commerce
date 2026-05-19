package com.e_commerce.controller;

import com.e_commerce.dto.CartRequestDTO;
import com.e_commerce.dto.CartResponseDTO;
import com.e_commerce.service.ICartService;
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
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Carrito", description = "Operaciones relacionadas con el carrito de compras.")
public class CartController {

    private final ICartService cartService;

    @Operation(summary = "Listar carritos", description = "Obtiene la lista completa de carritos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de carritos obtenida correctamente.")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CartResponseDTO>> getAllCart() {
        List<CartResponseDTO> cartResponseDTOList = cartService.findAll();
        return ResponseEntity.ok(cartResponseDTOList);
    }

    @Operation(summary = "Buscar carrito por ID", description = "Devuelve un carrito según su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito encontrado."),
            @ApiResponse(responseCode = "404", description = "No se encontró un carrito con el ID indicado.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CartResponseDTO> getCartById(@PathVariable Long id) {
        CartResponseDTO cartResponseDTO = cartService.findById(id);
        return ResponseEntity.ok(cartResponseDTO);
    }

    @Operation(summary = "Crear un carrito", description = "Registra un nuevo carrito.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Carrito creado exitosamente."),
            @ApiResponse(responseCode = "409", description = "El usuario ya tiene carrito asignado.")
    })
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CartResponseDTO> createCart(@RequestBody @Valid CartRequestDTO cartRequestDTO) {
        CartResponseDTO cartResponseDTO = cartService.save(cartRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponseDTO);
    }

    @Operation(summary = "Eliminar carrito", description = "Elimina un carrito existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Carrito eliminado correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró un carrito con el ID indicado.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity deleteCartById(@PathVariable Long id) {
        cartService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar carrito", description = "Modifica los datos de un carrito existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito actualizado correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró un carrito con el ID indicado."),
            @ApiResponse(responseCode = "409", description = "El usuario ya tiene carrito asignado.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CartResponseDTO> updateCartById(@PathVariable Long id,@RequestBody @Valid CartRequestDTO cartRequestDTO) {
        CartResponseDTO cartResponseDTO = cartService.updateById(id,cartRequestDTO);
        return ResponseEntity.ok(cartResponseDTO);
    }


}
