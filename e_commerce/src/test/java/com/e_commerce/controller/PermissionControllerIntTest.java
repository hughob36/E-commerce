package com.e_commerce.controller;

import com.e_commerce.dto.PermissionRequestDTO;
import com.e_commerce.model.Permission;
import com.e_commerce.repository.IPermissionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PermissionControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IPermissionRepository permissionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/permission - Should save a permission successfully")
    @WithMockUser(roles = {"ADMIN"})
    public void save_Permission() throws Exception {

        Permission permissionRequest = new Permission();
        permissionRequest.setPermissionName("CREATE");

        MvcResult mvcResult = mockMvc.perform(post("/api/permission")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(permissionRequest)))
                .andExpect(status().isCreated()) // O isCreated() si tu controller devuelve 201
                .andReturn();

        // 3. Assert: Verificamos en la base de datos real (H2)
        // Buscamos directamente por el nombre del permiso
        Permission permissionDB = permissionRepository.findAll().stream()
                .filter(p -> "CREATE".equals(p.getPermissionName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Permission 'CREATE' not found in database"));

        assertThat(permissionDB.getId()).isNotNull();
        assertThat(permissionDB.getPermissionName()).isEqualTo("CREATE");
    }

}
