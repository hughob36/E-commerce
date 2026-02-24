package com.e_commerce.controller;


import com.e_commerce.dto.PermissionRequestDTO;
import com.e_commerce.dto.PermissionResponseDTO;
import com.e_commerce.service.IPermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
//@PreAuthorize("denyAll()")
public class PermissionController {

    private final IPermissionService permissionService;

    @GetMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionResponseDTO>> getAllPermission() {
        return ResponseEntity.ok(permissionService.findAll());
    }

    @GetMapping("/{id}")
    //@PreAuthorize("permitAll()")
    public ResponseEntity<PermissionResponseDTO> getPermission(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PermissionResponseDTO> createPermission(@RequestBody @Valid PermissionRequestDTO permission) {
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.save(permission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePermission(@PathVariable Long id) {
        permissionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> updatePermissionById(@PathVariable Long id, @RequestBody @Valid PermissionRequestDTO permission) {
        return ResponseEntity.ok(permissionService.updateById(id, permission));
    }

}
