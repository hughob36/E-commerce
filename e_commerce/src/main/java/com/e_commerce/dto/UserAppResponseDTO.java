package com.e_commerce.dto;

import com.e_commerce.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAppResponseDTO {

    private Long id;
    private String name;
    private String lastName;
    private String username;
    private String password;
    private String email;

    private boolean enable;
    private boolean accountNotExpired;
    private boolean accountNotLocked;
    private boolean credentialNotExpired;

    private Set<Role> roleSet = new HashSet<>();
    private String phone;
    private String address; // Dirección completa (calle, número)
    private String city; // Ciudad
    private String state; // Estado/Provincia
    private String postalCode; // Código postal
    private String country; // País
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
