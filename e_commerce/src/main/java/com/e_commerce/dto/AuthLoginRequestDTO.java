package com.e_commerce.dto;


import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequestDTO(@NotBlank String username, String password) {
}
