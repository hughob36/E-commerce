package com.e_commerce.dto;

public record SuccessResponseDTO(String message, Object object) {

    public SuccessResponseDTO(String message) {
        this(message, null);
    }
    public SuccessResponseDTO(String message, Object object) {
        this.message = message;
        this.object = object;
    }

}
