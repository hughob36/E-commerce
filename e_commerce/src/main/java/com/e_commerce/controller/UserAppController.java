package com.e_commerce.controller;


import com.e_commerce.dto.SuccessResponseDTO;
import com.e_commerce.model.UserApp;
import com.e_commerce.service.IRoleService;
import com.e_commerce.service.IUserAppService;
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
    public ResponseEntity<List<UserApp>> getUsers() {
        List<UserApp> userAppList = userAppService.findAll();
        return ResponseEntity.ok(userAppList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserApp> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userAppService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserApp> createUserApp(@RequestBody UserApp userApp) {
        UserApp newUserApp = userAppService.save(userApp);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserApp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable Long id) {
        userAppService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserApp> updateUserById(@PathVariable Long id, @RequestBody UserApp userApp) {
        UserApp newUserApp = userAppService.updateById(id,userApp);
        return ResponseEntity.ok(newUserApp);
    }

}
