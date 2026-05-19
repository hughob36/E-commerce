package com.e_commerce.controller;


import com.e_commerce.dto.UserAppRequestDTO;
import com.e_commerce.dto.UserAppResponseDTO;
import com.e_commerce.service.IRoleService;
import com.e_commerce.service.IUserAppService;
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
@RequestMapping("/api/user")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios.")
public class UserAppController {

    private final IUserAppService userAppService;
    private final IRoleService roleService;

    @Operation(summary = "Listar usuarios", description = "Obtiene la lista completa de usuarios registrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente.")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserAppResponseDTO>> getUsers() {
        return ResponseEntity.ok(userAppService.findAll());
    }

    @Operation(summary = "Buscar usuario por ID", description = "Devuelve un usuario según su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado."),
            @ApiResponse(responseCode = "404", description = "No se encontró un usuario con el ID indicado.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserAppResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userAppService.findById(id));
    }

    @Operation(summary = "Crear un usuario", description = "Registra un nuevo usuario con roles asignados.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente."),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe.")
    })
    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserAppResponseDTO> createUserApp(@RequestBody  @Valid UserAppRequestDTO userAppRequestDTO) {
        UserAppResponseDTO newUserApp = userAppService.save(userAppRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserApp);
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró un usuario con el ID indicado.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity deleteUserById(@PathVariable Long id) {
        userAppService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar usuario", description = "Modifica los datos de un usuario existente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró un usuario con el ID indicado."),
            @ApiResponse(responseCode = "409", description = "El nombre de usuario ya está en uso.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserAppResponseDTO> updateUserById(@PathVariable Long id, @RequestBody @Valid UserAppRequestDTO userAppRequestDTO) {
        UserAppResponseDTO newUserApp = userAppService.updateById(id,userAppRequestDTO);
        return ResponseEntity.ok(newUserApp);
    }



}
