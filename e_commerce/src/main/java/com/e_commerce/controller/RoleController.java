package com.e_commerce.controller;


import com.e_commerce.dto.RoleRequestDTO;
import com.e_commerce.dto.RoleResponseDTO;

import com.e_commerce.service.IRoleService;
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
@RequestMapping("/api/role")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Roles", description = "Operaciones relacionadas con la gestión de roles y permisos.")
public class RoleController {

    private final IRoleService roleService;

    @Operation(summary = "Listar roles", description = "Obtiene la lista completa de roles registrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de roles obtenida correctamente.")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleResponseDTO>> getRole() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @Operation(summary = "Buscar rol por ID", description = "Devuelve un rol según su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rol encontrado."),
            @ApiResponse(responseCode = "404", description = "No se encontró un rol con el ID indicado.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<RoleResponseDTO> getRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @Operation(summary = "Crear rol", description = "Registra un nuevo rol con permisos asignados.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Rol creado correctamente."),
            @ApiResponse(responseCode = "409", description = "El rol ya existe o hay permisos duplicados.")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponseDTO> createRole(@RequestBody @Valid RoleRequestDTO role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.save(role));
    }

    @Operation(summary = "Eliminar rol", description = "Elimina un rol existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Rol eliminado correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró un rol con el ID indicado.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteRoleById(@PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar rol", description = "Modifica los datos de un rol existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rol actualizado correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró un rol con el ID indicado"),
            @ApiResponse(responseCode = "409", description = "El nombre del rol ya está en uso.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponseDTO> updateRoleById(@PathVariable Long id, @RequestBody @Valid RoleRequestDTO roleRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.updateById(id,roleRequestDTO));
    }

}
