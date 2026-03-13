package com.e_commerce.controller;

import com.e_commerce.dto.PermissionRequestDTO;
import com.e_commerce.dto.PermissionResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.service.IPermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PermissionController.class)
public class PermissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPermissionService permissionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/permission - Should return list of permissions with 200 OK when data exists")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getAllPermission_ShouldReturnPermissionResponseDTOList() throws Exception {
        // 1. Arrange (Preparación)
        PermissionResponseDTO dto = new PermissionResponseDTO(1L, "CREATE");
        List<PermissionResponseDTO> dtoList = List.of(dto);

        when(permissionService.findAll()).thenReturn(dtoList);

        // 2. Act (Acción) & 3. Assert (Verificación)
        mockMvc.perform(get("/api/permission").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].permissionName").value("CREATE"))
                // Un toque extra: verificar que no vengan campos nulos inesperados
                .andExpect(jsonPath("$[0].permissionName").exists());

        verify(permissionService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/permission - Should return 401 Unauthorized when no user is authenticated")
    public void getAllPermission_WithoutUser_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/permission"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/permission/{id} - Should return permission details with 200 OK when ID exists")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getPermission_ShouldReturnPermissionResponseDTO() throws Exception {

        Long id = 1L;
        PermissionResponseDTO permissionResponseDTO = new PermissionResponseDTO(id,"CREATE");
        when(permissionService.findById(id)).thenReturn(permissionResponseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/permission/{id}", id).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.permissionName").value("CREATE"));

        verify(permissionService, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/permission/{id} - Should return 404 Not Found when permission ID does not exist")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getPermissionById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {
        // Arrange
        Long id = 99L;

        when(permissionService.findById(id)).thenThrow(new ResourceNotFoundException("Id '" + id + "' not found."));
        // Act & Assert
        mockMvc.perform(get("/api/permission/{id}", id))
                .andExpect(status().isNotFound());

        verify(permissionService, times(1)).findById(id);
    }

    @Test
    @DisplayName("POST /api/permission - Should create a permission and return 201 Created")
    @WithMockUser(roles = {"ADMIN"})
    public void createPermission_ShouldReturnCreated() throws Exception {
        // Arrange
        PermissionRequestDTO requestDTO = new PermissionRequestDTO("UPDATE");
        PermissionResponseDTO responseDTO = new PermissionResponseDTO(2L, "UPDATE");

        when(permissionService.save(any(PermissionRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/permission")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))) // Convertimos el objeto a JSON String
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.permissionName").value("UPDATE"));
    }

    @Test
    @DisplayName("POST /api/permission - Should return 400 Bad Request when validation fails")
    @WithMockUser(roles = {"ADMIN"})
    public void createPermission_ShouldReturnBadRequest_WhenNameIsEmpty() throws Exception {
        // Arrange: Enviamos un DTO con nombre vacío (asumiendo que tienes @NotBlank)
        PermissionRequestDTO invalidRequest = new PermissionRequestDTO("");
        // Act & Assert
        mockMvc.perform(post("/api/permission")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        // Verify: El servicio nunca debería ser llamado si la validación falla en el controller
        verify(permissionService, never()).save(any());
    }

    @Test
    @DisplayName("DELETE /api/permission/{id} - Should return 24 No Content when successful")
    @WithMockUser(roles = {"ADMIN"})
    public void deletePermission_ShouldReturnNoContent_WhenIdExists() throws Exception {
        // Arrange
        Long id = 1L;
        // Como el método es void y no lanza excepción, usamos doNothing()
        doNothing().when(permissionService).deleteById(id);
        // Act & Assert
        mockMvc.perform(delete("/api/permission/{id}", id)
                        .with(csrf())) // Importante: DELETE también requiere CSRF
                .andExpect(status().isNoContent());
        // Verificamos que el servicio se llamó exactamente una vez con ese ID
        verify(permissionService, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/permission/{id} - Should return 404 Not Found when ID does not exist")
    @WithMockUser(roles = {"ADMIN"})
    public void deletePermission_ShouldReturnNotFound_WhenIdDoesNotExist() throws Exception {
        // Arrange
        Long id = 99L;
        // Usamos doThrow porque deleteById es un método void
        doThrow(new ResourceNotFoundException("Id '" + id + "' not found."))
                .when(permissionService).deleteById(id);

        // Act & Assert
        mockMvc.perform(delete("/api/permission/{id}", id)
                        .with(csrf())) // Protegemos contra CSRF
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Id '99' not found."));

        verify(permissionService, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("PUT /api/permission/{id} - Should return 400 Bad Request when update data is invalid")
    @WithMockUser(roles = {"ADMIN"})
    public void updatePermission_ShouldReturnBadRequest_WhenDataInvalid() throws Exception {
        PermissionRequestDTO invalidRequest = new PermissionRequestDTO("");

        mockMvc.perform(put("/api/permission/{id}", 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/permission/{id} - Should return 404 Not Found when updating non-existent ID")
    @WithMockUser(roles = {"ADMIN"})
    public void updatePermission_ShouldReturnNotFound_WhenIdDoesNotExist() throws Exception {
        Long id = 99L;
        PermissionRequestDTO requestDTO = new PermissionRequestDTO("NEW_NAME");

        when(permissionService.updateById(eq(id), any())).thenThrow(new ResourceNotFoundException("Id '99' not found."));

        mockMvc.perform(put("/api/permission/{id}", id)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Id '99' not found."));
    }

}
