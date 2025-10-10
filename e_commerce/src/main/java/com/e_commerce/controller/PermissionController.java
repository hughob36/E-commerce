package com.e_commerce.controller;

import com.e_commerce.dto.ErrorResponseDTO;
import com.e_commerce.dto.SuccessResponseDTO;
import com.e_commerce.model.Permission;
import com.e_commerce.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermission() {
        List<Permission> permissionList = permissionService.findAll();
        return ResponseEntity.ok(permissionList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermission(@PathVariable Long id) {
        return permissionService.findById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createPermission(@RequestBody Permission permission) {

        Permission newPermission = permissionService.save(permission);
        if(newPermission != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                     .body(new SuccessResponseDTO("Created Permission.", newPermission));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                     .body(new ErrorResponseDTO("Permission not created."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePermission(@PathVariable Long id) {

        if(permissionService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO("Id '"+ id + "' was found."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePermissionById(@PathVariable Long id, @RequestBody Permission permission) {

        try {
            Permission newPermission = permissionService.updateById(id, permission);

            if(newPermission != null) {
                return ResponseEntity.ok(newPermission);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO("Permission not found."));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Permission '" + permission.getPermissionName() + "' already exists.");
        }
    }


}
