package com.e_commerce.controller;

import com.e_commerce.dto.PermissionRequestDTO;
import com.e_commerce.dto.RoleRequestDTO;
import com.e_commerce.model.Permission;
import com.e_commerce.model.Role;
import com.e_commerce.repository.IPermissionRepository;
import com.e_commerce.repository.IRoleRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RoleControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IPermissionRepository permissionRepository;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/role - Should return all roles for ADMIN")
    @WithMockUser(roles = {"ADMIN"})
    public void getAllRole_Success() throws Exception {

        roleRepository.deleteAll();
        Set<Permission> permissionSet = new HashSet<>();
        Role role1 = new Role();
        role1.setRole("ADM");
        role1.setPermissionSet(permissionSet);
        Role role2 = new Role();
        role2.setRole("USER");
        role2.setPermissionSet(permissionSet);
        roleRepository.saveAll(java.util.List.of(role1, role2));

        mockMvc.perform(get("/api/role")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    assertThat(json).contains("ADM");
                    assertThat(json).contains("USER");
                });
    }

    @Test
    @DisplayName("GET /api/role - Should return 403 Forbidden for non-admin users")
    @WithMockUser(roles = {"USER"})
    public void getAllRole_Forbidden() throws Exception {

        mockMvc.perform(get("/api/role")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/role/{id} - Should allow access to ADMIN role")
    @WithMockUser(roles = {"ADMIN"})
    public void getRoleById_AdminSuccess() throws Exception {
        // 1. Arrange
        Role role = new Role();
        Set<Permission> permissionSet = new HashSet<>();
        role.setRole("ADM_VIEW");
        role.setPermissionSet(permissionSet);
        Role saved = roleRepository.save(role);

        // 2. Act & Assert
        mockMvc.perform(get("/api/role/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADM_VIEW"));
    }

    @Test
    @DisplayName("GET /api/role/{id} - Should allow access to USER role")
    @WithMockUser(roles = {"USER"})
    public void getRoleById_UserSuccess() throws Exception {
        // 1. Arrange
        Role role = new Role();
        Set<Permission> permissionSet = new HashSet<>();
        role.setRole("USER_VIEW");
        role.setPermissionSet(permissionSet);
        Role saved = roleRepository.save(role);

        // 2. Act & Assert
        mockMvc.perform(get("/api/role/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("USER_VIEW"));
    }

    @Test
    @DisplayName("GET /api/role/{id} - Should return 403 for roles other than USER or ADMIN")
    @WithMockUser(roles = {"GUEST"})
    public void getRoleById_ForbiddenForGuest() throws Exception {
        mockMvc.perform(get("/api/role/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/role - Should save a role successfully")
    @WithMockUser(roles = {"ADMIN"})
    public void save_Permission() throws Exception {

        Permission permission = new Permission();
        permission.setPermissionName("READ_PRIVILEGE");
        permissionRepository.save(permission);

        Role roleRequest = new Role();
        roleRequest.setRole("ADMIN");

        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(permission);
        roleRequest.setPermissionSet(permissionSet);


        mockMvc.perform(post("/api/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
                .andExpect(status().isCreated());


        Role roleDB = roleRepository.findAll().stream()
                .filter(p -> "ADMIN".equals(p.getRole()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Role 'ADMIN' not found"));

        assertThat(roleDB.getId()).isNotNull();
        assertThat(roleDB.getRole()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("POST /api/role - Should return 409 conflict when permission name already exists.")
    @WithMockUser(roles = {"ADMIN"})
    public void save_Role_Conflict_DuplicateName() throws Exception {

        Permission permission = new Permission();
        permission.setPermissionName("READ_PRIVILEGE2");
        permissionRepository.save(permission);

        Role existingRole = new Role();
        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(permission);
        existingRole.setPermissionSet(permissionSet);
        existingRole.setRole("ADMIN2");
        roleRepository.save(existingRole);

        Role duplicateRole = new Role();
        duplicateRole.setRole("ADMIN2");
        duplicateRole.setPermissionSet(permissionSet);

        // 2. Act & Assert
        mockMvc.perform(post("/api/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(duplicateRole)))
                .andExpect(status().isConflict())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertThat(response).contains("error_detalle");
                });

        // 3. Verificación adicional: Que en la DB solo siga existiendo 1 registro
        long count = roleRepository.findAll().stream()
                .filter(p -> "ADMIN2".equals(p.getRole()))
                .count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("POST /api/role - Should return 403 Forbidden for non-admin users")
    @WithMockUser(roles = {"USER"})
    public void save_Role_Forbidden() throws Exception {

        Permission permission = new Permission();
        permission.setPermissionName("READ_PRIVILEGE3");
        permissionRepository.save(permission);

        Role role = new Role();
        role.setRole("DELETE");
        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(permission);
        role.setPermissionSet(permissionSet);

        mockMvc.perform(post("/api/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /api/role/{id} - Should delete role and return 204")
    @WithMockUser(roles = {"ADMIN"})
    public void deleteRole_Success() throws Exception {

        Permission permission = new Permission();
        permission.setPermissionName("READ_PRIVILEGE4");
        permissionRepository.save(permission);

        Role role = new Role();
        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(permission);
        role.setPermissionSet(permissionSet);
        role.setRole("TEMP_ROLE");
        Role saved = roleRepository.save(role);
        Long idToDelete = saved.getId();

        // 2. Act: Llamamos al endpoint de borrado
        mockMvc.perform(delete("/api/role/{id}", idToDelete))
                .andExpect(status().isNoContent());

        // 3. Assert: Verificamos que realmente desapareció de la DB
        boolean exists = roleRepository.existsById(idToDelete);
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("DELETE /api/role/{id} - Should return 404 when id does not exist")
    @WithMockUser(roles = {"ADMIN"})
    public void deleteRole_NotFound() throws Exception {
        Long nonExistentId = 999L;
        // Act & Assert
        mockMvc.perform(delete("/api/permission/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/role/{id} - Should update role name successfully")
    @WithMockUser(roles = {"ADMIN"})
    public void updatePermission_Success() throws Exception {

        Permission permission = new Permission();
        permission.setPermissionName("READ_PRIVILEGE5");
        permissionRepository.save(permission);

        Role original = new Role();
        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(permission);
        original.setPermissionSet(permissionSet);
        original.setRole("OLD_NAME");
        Role saved = roleRepository.save(original);
        Long id = saved.getId();

        // Preparamos el DTO con el nuevo dato
        RoleRequestDTO updateRequest = new RoleRequestDTO();
        updateRequest.setRole("NEW_NAME");
        updateRequest.setPermissionSet(permissionSet);

        // 2. Act & Assert
        mockMvc.perform(put("/api/role/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("NEW_NAME"));

        // 3. Verificación extra en la DB
        Role updatedInDb = roleRepository.findById(id).get();
        assertThat(updatedInDb.getRole()).isEqualTo("NEW_NAME");
    }

    @Test
    @DisplayName("PUT /api/role/{id} - Should return 404 when trying to update non-existent id")
    @WithMockUser(roles = {"ADMIN"})
    public void updatePermission_NotFound() throws Exception {

        Permission permission = new Permission();
        permission.setPermissionName("READ_PRIVILEGE6");
        permissionRepository.save(permission);

        RoleRequestDTO updateRequest = new RoleRequestDTO();
        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(permission);
        updateRequest.setRole("ANY_NAME");
        updateRequest.setPermissionSet(permissionSet);

        mockMvc.perform(put("/api/role/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

}
