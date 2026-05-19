package com.e_commerce.controller;

import com.e_commerce.dto.OrderRequestDTO;
import com.e_commerce.dto.OrderResponseDTO;
import com.e_commerce.service.IOrderService;
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
@RequestMapping("/api/order")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Orden", description = "Operaciones relacionadas con la gestión de ordenes.")
public class OrderController {

    private final IOrderService orderService;

    @Operation(summary = "Listar orden", description = "Obtiene la lista completa de ordenes registradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de ordenes obtenida correctamente.")
    })
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrder() {
        List<OrderResponseDTO> orderResponseDTOList = orderService.findAll();
        return ResponseEntity.ok(orderResponseDTOList);
    }

    @Operation(summary = "Buscar orden por ID", description = "Devuelve una orden según su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden encontrada."),
            @ApiResponse(responseCode = "404", description = "No se encontró la orden con el ID indicado.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        OrderResponseDTO orderResponseDTO = orderService.findById(id);
        return ResponseEntity.ok(orderResponseDTO);
    }

    @Operation(summary = "Crear una orden", description = "Registra una nueva orden.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Orden creado exitosamente.")
    })
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO orderResponseDTO = orderService.save(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);
    }

    @Operation(summary = "Eliminar orden", description = "Elimina una orden existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Orden eliminada correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró la orden con el ID indicado.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity deleteOrderById(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar orden", description = "Modifica los datos de la orden existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden actualizada correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró la orden con el ID indicado.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> updateOrderById(@PathVariable Long id, @RequestBody @Valid OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO orderResponseDTO = orderService.updateById(id,orderRequestDTO);
        return ResponseEntity.ok(orderResponseDTO);
    }
}
