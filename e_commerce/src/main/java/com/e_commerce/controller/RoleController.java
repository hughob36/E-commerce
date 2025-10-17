package com.e_commerce.controller;

import com.e_commerce.dto.ErrorResponseDTO;
import com.e_commerce.dto.SuccessResponseDTO;
import com.e_commerce.model.Permission;
import com.e_commerce.model.Role;
import com.e_commerce.service.IPermissionService;
import com.e_commerce.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;
    private final IPermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<Role>> getRole() {
        List<Role> roleList = roleService.findAll();
        return ResponseEntity.ok(roleList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role role) {

        Set<Permission> permissionSet = new HashSet<>();

        for(Permission permission : role.getPermissionSet()) {
            permissionService.findById(permission.getId()).ifPresent(permissionSet::add);
        }

        try {
            role.setPermissionSet(permissionSet);
            Role newRole = roleService.save(role);
            return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new SuccessResponseDTO("Role created.", newRole));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                       .body(new ErrorResponseDTO("Role already exists."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoleById(@PathVariable Long id) {
        if(roleService.deleteById(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new SuccessResponseDTO("Delete role."));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO("Id '" + id + "' not found."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoleById(@PathVariable Long id, @RequestBody Role role) {

        Set<Permission> permissionSet = new HashSet<>();
        for(Permission permission : role.getPermissionSet()) {
            permissionService.findById(permission.getId()).ifPresent(permissionSet::add);
        }

        try {
            role.setPermissionSet(permissionSet);
            Role roleFound = roleService.updateById(id,role);

            if(roleFound != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new SuccessResponseDTO("Role update.", roleFound));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO("Role '" + role.getRole() + "' not found."));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponseDTO("Role '" + role.getRole() + "' already exists."));
        }

    }

}
