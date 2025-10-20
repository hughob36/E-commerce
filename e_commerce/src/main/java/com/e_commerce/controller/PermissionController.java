package com.e_commerce.controller;

import com.e_commerce.dto.ErrorResponseDTO;
import com.e_commerce.dto.SuccessResponseDTO;
import com.e_commerce.model.Permission;
import com.e_commerce.service.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final IPermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermission() {
        List<Permission> permissionList = permissionService.findAll();
        return ResponseEntity.ok(permissionList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermission(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.save(permission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePermission(@PathVariable Long id) {
        permissionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Permission> updatePermissionById(@PathVariable Long id, @RequestBody Permission permission) {
        return ResponseEntity.ok(permissionService.updateById(id, permission));
    }

}
