package com.e_commerce.controller;


import com.e_commerce.dto.PermissionRequestDTO;
import com.e_commerce.dto.PermissionResponseDTO;
import com.e_commerce.service.IPermissionService;
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
@RequestMapping("/api/permission")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Permisos", description = "Operaciones relacionadas con la gestión de permisos de usuario.")
public class PermissionController {

    private final IPermissionService permissionService;

    @Operation(summary = "Listar permisos", description = "Obtiene la lista completa de permisos disponibles.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de permisos obtenida correctamente.")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionResponseDTO>> getAllPermission() {
        return ResponseEntity.ok(permissionService.findAll());
    }

    @Operation(summary = "Buscar permiso por ID",
            description = "Devuelve un permiso específico según su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Permiso encontrado."),
            @ApiResponse(responseCode = "404", description = "No se encontró un permiso con el ID indicado.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PermissionResponseDTO> getPermission(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.findById(id));
    }

    @Operation(summary = "Crear un nuevo permiso",
            description = "Registra un nuevo permiso en la base de datos. Si el nombre ya existe, devuelve error de conflicto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Permiso creado exitosamente."),
            @ApiResponse(responseCode = "409", description = "El permiso ya existe en la base de datos.")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponseDTO> createPermission(@RequestBody @Valid PermissionRequestDTO permission) {
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.save(permission));
    }

    @Operation(summary = "Eliminar un permiso",
            description = "Elimina un permiso existente según su ID. Devuelve error si no se encuentra.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Permiso eliminado correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró un permiso con el ID indicado.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deletePermission(@PathVariable Long id) {
        permissionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar un permiso",
            description = "Modifica los datos de un permiso existente. Devuelve error si no se encuentra o si el nombre ya está en uso.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Permiso actualizado correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró un permiso con el ID indicado."),
            @ApiResponse(responseCode = "409", description = "El nombre del permiso ya está en uso.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponseDTO> updatePermissionById(@PathVariable Long id, @RequestBody @Valid PermissionRequestDTO permission) {
        return ResponseEntity.ok(permissionService.updateById(id, permission));
    }

}
