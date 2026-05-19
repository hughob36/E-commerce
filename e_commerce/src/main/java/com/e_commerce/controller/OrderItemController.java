package com.e_commerce.controller;

import com.e_commerce.dto.OrderItemRequestDTO;
import com.e_commerce.dto.OrderItemResponseDTO;
import com.e_commerce.service.IOrderItemService;
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
@RequestMapping("/api/orderItem")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Orden Items", description = "Operaciones relacionadas con la gestión de orden items.")
public class OrderItemController {

    private final IOrderItemService orderItemService;

    @Operation(summary = "Listar orden items", description = "Obtiene la lista completa de los items de la orden.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de items obtenida correctamente.")
    })
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<OrderItemResponseDTO>> getAllOrderItem() {
        List<OrderItemResponseDTO> orderItemResponseDTOList = orderItemService.findAll();
        return ResponseEntity.ok(orderItemResponseDTOList);
    }

    @Operation(summary = "Buscar items de la orden por ID", description = "Devuelve un items de una orden según su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Items de orden encontrado."),
            @ApiResponse(responseCode = "404", description = "No se encontró un items de la orden con el ID indicado.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<OrderItemResponseDTO> getOrderItemById(@PathVariable Long id) {
        OrderItemResponseDTO orderItemResponseDTO = orderItemService.findById(id);
        return ResponseEntity.ok(orderItemResponseDTO);
    }

    @Operation(summary = "Crear un items en orden", description = "Registra un nuevo items en una orden.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Items creado exitosamente.")
    })
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<OrderItemResponseDTO> createOrderItem(@RequestBody @Valid OrderItemRequestDTO orderItemRequestDTO) {
        OrderItemResponseDTO orderItemResponseDTO = orderItemService.save(orderItemRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderItemResponseDTO);
    }

    @Operation(summary = "Eliminar items", description = "Elimina un items existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Items eliminado correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró un items con el ID indicado.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity deleteOrderItemById(@PathVariable Long id) {
        orderItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar items", description = "Modifica los datos de un items existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Items actualizado correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró un items con el ID indicado.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<OrderItemResponseDTO> updateOrderItemById(@PathVariable Long id,@RequestBody @Valid OrderItemRequestDTO orderItemRequestDTO) {
        OrderItemResponseDTO orderItemResponseDTO = orderItemService.updateById(id,orderItemRequestDTO);
        return ResponseEntity.ok(orderItemResponseDTO);
    }
}
