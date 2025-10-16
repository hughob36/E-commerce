package com.e_commerce.controller;

import com.e_commerce.dto.AuthLoginRequestDTO;
import com.e_commerce.dto.AuthResponseDTO;
import com.e_commerce.service.UserDetailsServiseImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutheticationController {

    @Autowired
    private UserDetailsServiseImpl userDetailsServise;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@RequestBody @Valid AuthLoginRequestDTO authLoginRequestDTO) {
            return new ResponseEntity(this.userDetailsServise.loginUser(authLoginRequestDTO), HttpStatus.OK);
    }

}
