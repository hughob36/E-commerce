package com.e_commerce.controller;


import com.e_commerce.dto.UserAppRequestDTO;
import com.e_commerce.dto.UserAppResponseDTO;

import com.e_commerce.service.IRoleService;
import com.e_commerce.service.IUserAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserAppController {

    private final IUserAppService userAppService;
    private final IRoleService roleService;

    @GetMapping
    public ResponseEntity<List<UserAppResponseDTO>> getUsers() {
        return ResponseEntity.ok(userAppService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAppResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userAppService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserAppResponseDTO> createUserApp(@RequestBody  @Valid UserAppRequestDTO userAppRequestDTO) {
        UserAppResponseDTO newUserApp = userAppService.save(userAppRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserApp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable Long id) {
        userAppService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAppResponseDTO> updateUserById(@PathVariable Long id, @RequestBody @Valid UserAppRequestDTO userAppRequestDTO) {
        UserAppResponseDTO newUserApp = userAppService.updateById(id,userAppRequestDTO);
        return ResponseEntity.ok(newUserApp);
    }

}
