package com.e_commerce.controller;

import com.e_commerce.dto.RoleRequestDTO;
import com.e_commerce.model.Permission;
import com.e_commerce.model.Role;
import com.e_commerce.model.UserApp;
import com.e_commerce.repository.IRoleRepository;
import com.e_commerce.repository.IUserAppRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;


import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserAppControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserAppRepository userAppRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/user - Should return all user for ADMIN")
    @WithMockUser(roles = {"ADMIN"})
    public void getAllUserApp_Success() throws Exception {

        userAppRepository.deleteAll();
        roleRepository.deleteAll();
        Set<Permission> permissionSet = new HashSet<>();
        Role role1 = new Role();
        role1.setRole("ADM");
        role1.setPermissionSet(permissionSet);
        roleRepository.save(role1);


        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role1);

        UserApp userApp1 = UserApp.builder()
                .name("Juan")
                .lastName("Perez")
                .username("ADMIN1")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .phone("111111")
                .address("user prueba")
                .city("cityPrueba")
                .state("estadoPrueba")
                .postalCode("3232")
                .country("countryPrueba")
                .build();

        UserApp userApp2 = UserApp.builder()
                .name("Juan")
                .lastName("Perez")
                .username("USER1")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .phone("111111")
                .address("user prueba")
                .city("cityPrueba")
                .state("estadoPrueba")
                .postalCode("3232")
                .country("countryPrueba")
                .build();

        userAppRepository.saveAll(java.util.List.of(userApp1, userApp2));

        mockMvc.perform(get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].username", hasItem("ADMIN1")))
                .andExpect(jsonPath("$[*].username", hasItem("USER1")));
    }

    @Test
    @DisplayName("GET /api/user - Should return 403 Forbidden for non-admin users")
    @WithMockUser(roles = {"USER"})
    public void getAllUserApp_Forbidden() throws Exception {

        mockMvc.perform(get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/user/{id} - Should allow access to ADMIN role")
    @WithMockUser(roles = {"ADMIN"})
    public void getUserAppById_AdminSuccess() throws Exception {
        // 1. Arrange
        userAppRepository.deleteAll();
        roleRepository.deleteAll();
        Set<Permission> permissionSet = new HashSet<>();
        Role role = new Role();
        role.setRole("ADM");
        role.setPermissionSet(permissionSet);
        roleRepository.save(role);

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        UserApp userApp = UserApp.builder()
                .name("Juan")
                .lastName("Perez")
                .username("ADMIN")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .phone("111111")
                .address("user prueba")
                .city("cityPrueba")
                .state("estadoPrueba")
                .postalCode("3232")
                .country("countryPrueba")
                .build();

        UserApp saved = userAppRepository.save(userApp);

        // 2. Act & Assert
        mockMvc.perform(get("/api/user/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("ADMIN"));
    }

    @Test
    @DisplayName("GET /api/user/{id} - Should allow access to USER role")
    @WithMockUser(roles = {"USER"})
    public void getUserAppById_UserSuccess() throws Exception {
        // 1. Arrange
        userAppRepository.deleteAll();
        roleRepository.deleteAll();
        Set<Permission> permissionSet = new HashSet<>();
        Role role = new Role();
        role.setRole("ADM");
        role.setPermissionSet(permissionSet);
        roleRepository.save(role);

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        UserApp userApp = UserApp.builder()
                .name("Juan")
                .lastName("Perez")
                .username("USER")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .phone("111111")
                .address("user prueba")
                .city("cityPrueba")
                .state("estadoPrueba")
                .postalCode("3232")
                .country("countryPrueba")
                .build();

        UserApp saved = userAppRepository.save(userApp);

        // 2. Act & Assert
        mockMvc.perform(get("/api/user/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("USER"));
    }

    @Test
    @DisplayName("GET /api/user/{id} - Should return 403 for roles other than USER or ADMIN")
    @WithMockUser(roles = {"GUEST"})
    public void getUserAppById_ForbiddenForGuest() throws Exception {
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/user - Should save a user successfully")
    @WithMockUser(roles = {"ADMIN"})
    public void save_Permission() throws Exception {

        userAppRepository.deleteAll();
        roleRepository.deleteAll();
        Set<Permission> permissionSet = new HashSet<>();
        Role role = new Role();
        role.setRole("ADM");
        role.setPermissionSet(permissionSet);
        roleRepository.save(role);

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        UserApp userAppRequest = UserApp.builder()
                .name("Juan")
                .lastName("Perez")
                .username("ADMIN")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .phone("111111")
                .address("user prueba")
                .city("cityPrueba")
                .state("estadoPrueba")
                .postalCode("3232")
                .country("countryPrueba")
                .build();

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAppRequest)))
                .andExpect(status().isCreated());


        UserApp userAppDB = userAppRepository.findAll().stream()
                .filter(p -> "ADMIN".equals(p.getUsername()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Role 'ADMIN' not found"));

        assertThat(userAppDB.getId()).isNotNull();
        assertThat(userAppDB.getUsername()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("POST /api/user - Should return 409 conflict when username name already exists.")
    @WithMockUser(roles = {"ADMIN"})
    public void saveUserAppConflictDuplicateUsername() throws Exception {

        userAppRepository.deleteAll();
        roleRepository.deleteAll();
        Set<Permission> permissionSet = new HashSet<>();
        Role role = new Role();
        role.setRole("ADM");
        role.setPermissionSet(permissionSet);
        roleRepository.save(role);

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        UserApp existingUserApp = UserApp.builder()
                .name("Juan")
                .lastName("Perez")
                .username("ADMIN")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .phone("111111")
                .address("user prueba")
                .city("cityPrueba")
                .state("estadoPrueba")
                .postalCode("3232")
                .country("countryPrueba")
                .build();

        UserApp saved = userAppRepository.save(existingUserApp);

        UserApp duplicateUserApp = UserApp.builder()
                .name("Juan")
                .lastName("Perez")
                .username("ADMIN")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .phone("111111")
                .address("user prueba")
                .city("cityPrueba")
                .state("estadoPrueba")
                .postalCode("3232")
                .country("countryPrueba")
                .build();

        // 2. Act & Assert
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(duplicateUserApp)))
                .andExpect(status().isConflict())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertThat(response).contains("error_detalle");
                });

        long count = userAppRepository.findAll().stream()
                .filter(p -> "ADMIN".equals(p.getUsername()))
                .count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("POST /api/user - Should return 403 Forbidden for non-admin users")
    @WithMockUser(roles = {"USER"})
    public void saveUserAppForbidden() throws Exception {

        userAppRepository.deleteAll();
        roleRepository.deleteAll();
        Set<Permission> permissionSet = new HashSet<>();
        Role role = new Role();
        role.setRole("ADM");
        role.setPermissionSet(permissionSet);
        roleRepository.save(role);

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        UserApp userApp = UserApp.builder()
                .name("Juan")
                .lastName("Perez")
                .username("ADMIN")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .phone("111111")
                .address("user prueba")
                .city("cityPrueba")
                .state("estadoPrueba")
                .postalCode("3232")
                .country("countryPrueba")
                .build();

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userApp)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /api/user/{id} - Should delete user and return 204")
    @WithMockUser(roles = {"ADMIN"})
    public void deleteUserApp_Success() throws Exception {

        userAppRepository.deleteAll();
        roleRepository.deleteAll();
        Set<Permission> permissionSet = new HashSet<>();
        Role role = new Role();
        role.setRole("ADM");
        role.setPermissionSet(permissionSet);
        roleRepository.save(role);

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        UserApp userApp = UserApp.builder()
                .name("Juan")
                .lastName("Perez")
                .username("ADMIN")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .phone("111111")
                .address("user prueba")
                .city("cityPrueba")
                .state("estadoPrueba")
                .postalCode("3232")
                .country("countryPrueba")
                .build();

        UserApp saved = userAppRepository.save(userApp);
        Long idToDelete = saved.getId();

        mockMvc.perform(delete("/api/user/{id}", idToDelete))
                .andExpect(status().isNoContent());

        boolean exists = userAppRepository.existsById(idToDelete);
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("DELETE /api/user/{id} - Should return 404 when id does not exist")
    @WithMockUser(roles = {"ADMIN"})
    public void deleteUserApp_NotFound() throws Exception {
        Long nonExistentId = 999L;

        mockMvc.perform(delete("/api/user/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/user/{id} - Should update username successfully")
    @WithMockUser(roles = {"ADMIN"})
    public void updateUserSuccess() throws Exception {

        userAppRepository.deleteAll();
        roleRepository.deleteAll();
        Set<Permission> permissionSet = new HashSet<>();
        Role role = new Role();
        role.setRole("ADM");
        role.setPermissionSet(permissionSet);
        roleRepository.save(role);

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        UserApp original = UserApp.builder()
                .name("Juan")
                .lastName("Perez")
                .username("OLD_NAME")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .phone("111111")
                .address("user prueba")
                .city("cityPrueba")
                .state("estadoPrueba")
                .postalCode("3232")
                .country("countryPrueba")
                .build();

        UserApp saved = userAppRepository.save(original);
        Long id = saved.getId();

        UserApp updateRequest = UserApp.builder()
                .name("Juan")
                .lastName("Perez")
                .username("NEW_NAME")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .phone("111111")
                .address("user prueba")
                .city("cityPrueba")
                .state("estadoPrueba")
                .postalCode("3232")
                .country("countryPrueba")
                .build();

        // 2. Act & Assert
        mockMvc.perform(put("/api/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("NEW_NAME"));

        UserApp updatedInDb = userAppRepository.findById(id).get();
        assertThat(updatedInDb.getUsername()).isEqualTo("NEW_NAME");
    }

    @Test
    @DisplayName("PUT /api/user/{id} - Should return 404 when trying to update non-existent id")
    @WithMockUser(roles = {"ADMIN"})
    public void updateUserApp_NotFound() throws Exception {

        userAppRepository.deleteAll();
        roleRepository.deleteAll();
        Set<Permission> permissionSet = new HashSet<>();
        Role role = new Role();
        role.setRole("ADM");
        role.setPermissionSet(permissionSet);
        roleRepository.save(role);

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        UserApp updateRequest = UserApp.builder()
                .name("Juan")
                .lastName("Perez")
                .username("ANY_NAME")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .phone("111111")
                .address("user prueba")
                .city("cityPrueba")
                .state("estadoPrueba")
                .postalCode("3232")
                .country("countryPrueba")
                .build();

        mockMvc.perform(put("/api/user/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }



}
