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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
    @DisplayName("GET /api/permission - Should return all permissions for ADMIN")
    @WithMockUser(roles = {"ADMIN"})
    public void getAllPermissions_Success() throws Exception {
        // 1. Arrange: Limpiamos y cargamos datos de prueba
        permissionRepository.deleteAll();
        Permission p1 = new Permission();
        p1.setPermissionName("CREATE");
        Permission p2 = new Permission();
        p2.setPermissionName("UPDATE");
        permissionRepository.saveAll(java.util.List.of(p1, p2));

        // 2. Act & Assert
        mockMvc.perform(get("/api/permission")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    // Verificamos que los nombres de los permisos estén en el JSON
                    assertThat(json).contains("CREATE");
                    assertThat(json).contains("UPDATE");
                });
    }

    @Test
    @DisplayName("GET /api/permission - Should return 403 Forbidden for non-admin users")
    @WithMockUser(roles = {"USER"})
    public void getAllPermissions_Forbidden() throws Exception {

        mockMvc.perform(get("/api/permission")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/permission/{id} - Should allow access to ADMIN role")
    @WithMockUser(roles = {"ADMIN"})
    public void getPermissionById_AdminSuccess() throws Exception {
        // 1. Arrange
        Permission permission = new Permission();
        permission.setPermissionName("ADM_VIEW");
        Permission saved = permissionRepository.save(permission);

        // 2. Act & Assert
        mockMvc.perform(get("/api/permission/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.permissionName").value("ADM_VIEW"));
    }

    @Test
    @DisplayName("GET /api/permission/{id} - Should allow access to USER role")
    @WithMockUser(roles = {"USER"})
    public void getPermissionById_UserSuccess() throws Exception {
        // 1. Arrange
        Permission permission = new Permission();
        permission.setPermissionName("USER_VIEW");
        Permission saved = permissionRepository.save(permission);

        // 2. Act & Assert
        mockMvc.perform(get("/api/permission/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.permissionName").value("USER_VIEW"));
    }

    @Test
    @DisplayName("GET /api/permission/{id} - Should return 403 for roles other than USER or ADMIN")
    @WithMockUser(roles = {"GUEST"})
    public void getPermissionById_ForbiddenForGuest() throws Exception {
        mockMvc.perform(get("/api/permission/1"))
                .andExpect(status().isForbidden());
    }

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

    @Test
    @DisplayName("POST /api/permission - Should return 409 conflict when permission name already exists.")
    @WithMockUser(roles = {"ADMIN"})
    public void save_Permission_Conflict_DuplicateName() throws Exception {

        Permission existingPermission = new Permission();
        existingPermission.setPermissionName("READ_PRIVILEGE");
        permissionRepository.save(existingPermission);

        Permission duplicatePermission = new Permission();
        duplicatePermission.setPermissionName("READ_PRIVILEGE");

        // 2. Act & Assert
        mockMvc.perform(post("/api/permission")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(duplicatePermission)))
                .andExpect(status().isConflict()) // Verifica que el status sea 409
                .andExpect(result -> {
                    // Opcional: Verificar que el JSON contiene la clave que definiste en el Handler
                    String response = result.getResponse().getContentAsString();
                    assertThat(response).contains("error_detalle");
                });

        // 3. Verificación adicional: Que en la DB solo siga existiendo 1 registro
        long count = permissionRepository.findAll().stream()
                .filter(p -> "READ_PRIVILEGE".equals(p.getPermissionName()))
                .count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("POST /api/permission - Should return 403 Forbidden for non-admin users")
    @WithMockUser(roles = {"USER"})
    public void save_Permission_Forbidden() throws Exception {
        Permission permission = new Permission();
        permission.setPermissionName("DELETE");

        mockMvc.perform(post("/api/permission")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(permission)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /api/permission/{id} - Should delete permission and return 204")
    @WithMockUser(roles = {"ADMIN"})
    public void deletePermission_Success() throws Exception {
        // 1. Arrange: Creamos el permiso que vamos a eliminar
        Permission permission = new Permission();
        permission.setPermissionName("TEMP_PERMISSION");
        Permission saved = permissionRepository.save(permission);
        Long idToDelete = saved.getId();

        // 2. Act: Llamamos al endpoint de borrado
        mockMvc.perform(delete("/api/permission/{id}", idToDelete))
                .andExpect(status().isNoContent()); // Verifica el 204 que pusiste en el return

        // 3. Assert: Verificamos que realmente desapareció de la DB
        boolean exists = permissionRepository.existsById(idToDelete);
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("DELETE /api/permission/{id} - Should return 404 when id does not exist")
    @WithMockUser(roles = {"ADMIN"})
    public void deletePermission_NotFound() throws Exception {
        Long nonExistentId = 999L;
        // Act & Assert
        mockMvc.perform(delete("/api/permission/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/permission/{id} - Should update permission name successfully")
    @WithMockUser(roles = {"ADMIN"})
    public void updatePermission_Success() throws Exception {
        // 1. Arrange: Creamos el permiso original
        Permission original = new Permission();
        original.setPermissionName("OLD_NAME");
        Permission saved = permissionRepository.save(original);
        Long id = saved.getId();

        // Preparamos el DTO con el nuevo dato
        PermissionRequestDTO updateRequest = new PermissionRequestDTO();
        updateRequest.setPermissionName("NEW_NAME");

        // 2. Act & Assert
        mockMvc.perform(put("/api/permission/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.permissionName").value("NEW_NAME"));

        // 3. Verificación extra en la DB
        Permission updatedInDb = permissionRepository.findById(id).get();
        assertThat(updatedInDb.getPermissionName()).isEqualTo("NEW_NAME");
    }

    @Test
    @DisplayName("PUT /api/permission/{id} - Should return 404 when trying to update non-existent id")
    @WithMockUser(roles = {"ADMIN"})
    public void updatePermission_NotFound() throws Exception {
        PermissionRequestDTO updateRequest = new PermissionRequestDTO();
        updateRequest.setPermissionName("ANY_NAME");

        mockMvc.perform(put("/api/permission/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

}
