package com.e_commerce.controller;

import com.e_commerce.dto.AuthLoginRequestDTO;
import com.e_commerce.dto.AuthResponseDTO;
import com.e_commerce.service.AuthenticationService;
import com.e_commerce.service.UserDetailsServiseImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Operaciones relacionadas con el login y registro de usuarios.")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Inicio de sesión de usuarios",
            description = "Permite que un usuario registrado en la base de datos inicie sesión y obtenga su token de autenticación.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso."),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas o usuario no autorizado.")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@RequestBody @Valid AuthLoginRequestDTO authLoginRequestDTO) {
            return new ResponseEntity(this.authenticationService.loginUser(authLoginRequestDTO), HttpStatus.OK);
    }

}
