package com.e_commerce.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRequestDTO {

    @NotBlank(message = "Empty name.")
    @Size(min = 2, max = 40, message = "Long name must be between 2 and 40 characters.")
    private String permissionName;
}
