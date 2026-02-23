package com.e_commerce.controller;


import com.e_commerce.dto.RoleRequestDTO;
import com.e_commerce.dto.RoleResponseDTO;
import com.e_commerce.model.Role;

import com.e_commerce.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getRole() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@RequestBody RoleRequestDTO role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.save(role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteRoleById(@PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRoleById(@PathVariable Long id, @RequestBody RoleRequestDTO roleRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.updateById(id,roleRequestDTO));
    }

}
