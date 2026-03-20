package com.e_commerce.controller;

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
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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



}
