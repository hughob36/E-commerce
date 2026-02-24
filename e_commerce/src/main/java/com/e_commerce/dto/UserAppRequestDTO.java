package com.e_commerce.dto;

import com.e_commerce.model.Role;
import jakarta.validation.constraints.Email;
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
@NoArgsConstructor
@AllArgsConstructor
public class UserAppRequestDTO {

    @NotBlank(message = "Empty name.")
    @Size(min = 2, max = 40, message = "Long name must be between 2 and 40 characters.")
    private String name;

    @NotBlank(message = "Empty lastname.")
    @Size(min = 2, max = 40, message = "Long lastname must be between 2 and 40 characters.")
    private String lastName;

    @NotBlank(message = "Empty username.")
    @Size(min = 2, max = 40, message = "Long username must be between 2 and 40 characters.")
    private String username;

    @NotBlank(message = "Empty password.")
    @Size(min = 2, max = 40, message = "Long password must be between 2 and 40 characters.")
    private String password;

    @NotBlank(message = "Empty email.")
    @Email(message = "Invalid email format.")
    @Size(max = 100, message = "Email is too long.")
    private String email;

    private boolean enable;
    private boolean accountNotExpired;
    private boolean accountNotLocked;
    private boolean credentialNotExpired;

    @NotEmpty(message = "The user must have at least one role assigned.")
    private Set<Role> roleSet = new HashSet<>();

    @NotBlank(message = "Empty phone.")
    @Size(min = 2, max = 40, message = "Long phone must be between 2 and 40 characters.")
    private String phone;

    @NotBlank(message = "Empty address.")
    @Size(min = 2, max = 255, message = "Long address must be between 2 and 255 characters.")
    private String address; // Dirección completa (calle, número)

    @NotBlank(message = "Empty city.")
    @Size(min = 2, max = 40, message = "Long city must be between 2 and 40 characters.")
    private String city; // Ciudad

    @NotBlank(message = "Empty state.")
    @Size(min = 2, max = 40, message = "Long state must be between 2 and 40 characters.")
    private String state; // Estado/Provincia

    @NotBlank(message = "Empty postalcode.")
    @Size(min = 2, max = 40, message = "Long postalcode must be between 2 and 40 characters.")
    private String postalCode; // Código postal

    @NotBlank(message = "Empty country.")
    @Size(min = 2, max = 40, message = "Long country must be between 2 and 40 characters.")
    private String country; // País

    /*@CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;*/

}
