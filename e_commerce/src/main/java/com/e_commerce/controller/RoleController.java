package com.e_commerce.controller;


import com.e_commerce.dto.RoleRequestDTO;
import com.e_commerce.dto.RoleResponseDTO;

import com.e_commerce.service.IRoleService;
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
public class RoleController {

    private final IRoleService roleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleResponseDTO>> getRole() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<RoleResponseDTO> getRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponseDTO> createRole(@RequestBody @Valid RoleRequestDTO role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.save(role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteRoleById(@PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRoleById(@PathVariable Long id, @RequestBody @Valid RoleRequestDTO roleRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.updateById(id,roleRequestDTO));
    }

}
