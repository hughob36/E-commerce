package com.e_commerce.dto;


import com.e_commerce.model.Permission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequestDTO {

    @NotBlank(message = "Empty Role name.")
    @Size(min = 2, max = 40, message = "Long name must be between 2 and 40 characters.")
    private String role;

    @NotEmpty(message = "The role must have at least one permission assigned.")
    private Set<Permission> permissionSet = new HashSet<>();
}
