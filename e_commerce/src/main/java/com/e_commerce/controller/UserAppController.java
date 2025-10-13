package com.e_commerce.controller;

import com.e_commerce.dto.ErrorResponseDTO;
import com.e_commerce.dto.SuccessResponseDTO;
import com.e_commerce.model.Role;
import com.e_commerce.model.UserApp;
import com.e_commerce.service.IRoleService;
import com.e_commerce.service.IUserAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserAppController {

    @Autowired
    private IUserAppService userAppService;

    @Autowired
    private IRoleService roleService;

    @GetMapping
    public ResponseEntity<List<UserApp>> getUsers() {
        List<UserApp> userAppList = userAppService.findAll();
        return ResponseEntity.ok(userAppList);
    }

    @GetMapping
    public ResponseEntity<UserApp> getUser(@PathVariable Long id) {
        return userAppService.findById(id)
                    .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createUserApp(@RequestBody UserApp userApp) {

        Set<Role> roleSet = new HashSet<>();
        for (Role role : userApp.getRoleSet()) {
            roleService.findById(role.getId()).ifPresent(roleSet::add);
        }

        try {
            userApp.setRoleSet(roleSet);
            UserApp newUserApp = userAppService.save(userApp);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new SuccessResponseDTO("created user.", newUserApp));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponseDTO("Username already exists."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {

        if(userAppService.deleteById(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new SuccessResponseDTO("Delete user."));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO("User not fund."));
    }

}
