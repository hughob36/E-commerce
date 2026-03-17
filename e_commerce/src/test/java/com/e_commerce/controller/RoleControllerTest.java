package com.e_commerce.controller;

import com.e_commerce.dto.PermissionRequestDTO;
import com.e_commerce.dto.PermissionResponseDTO;
import com.e_commerce.dto.RoleRequestDTO;
import com.e_commerce.dto.RoleResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.model.Permission;
import com.e_commerce.service.IRoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(RoleController.class)
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IRoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/role - Should return list of roles with 200 OK when data exists")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getAllRole_ShouldReturnRoleResponseDTOList() throws Exception {

        Set<Permission> permissions = new HashSet<>();
        permissions.add(new Permission());
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L,"ADM",permissions);
        List<RoleResponseDTO> roleResponseDTOList = List.of(roleResponseDTO);

        when(roleService.findAll()).thenReturn(roleResponseDTOList);

        mockMvc.perform(get("/api/role").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].role").value("ADM"))
                .andExpect(jsonPath("$[0].permissionSet").isArray())
                .andExpect(jsonPath("$[0].permissionSet", not(empty())))
                .andExpect(jsonPath("$[0].role").exists());

        verify(roleService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/role - Should return 401 Unauthorized when no user is authenticated")
    public void getAllRole_WithoutUser_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/role"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/role/{id} - Should return role details with 200 OK when ID exists")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getRoleById_ShouldReturnRoleResponseDTO() throws Exception {

        Long id = 1L;
        Set<Permission> permissions = new HashSet<>();
        permissions.add(new Permission());
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L,"ADM",permissions);

        when(roleService.findById(id)).thenReturn(roleResponseDTO);

        mockMvc.perform(get("/api/role/{id}",id).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.role").value("ADM"))
                .andExpect(jsonPath("$.permissionSet").isArray())
                .andExpect(jsonPath("$.permissionSet", not(empty())));

        verify(roleService, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/role/{id} - Should return 404 Not Found when permission ID does not exist")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getRoleById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {

        Long id = 99L;

        when(roleService.findById(id)).thenThrow(new ResourceNotFoundException("Role '"+ id +"' not found."));

        mockMvc.perform(get("/api/role/{id}", id))
                .andExpect(status().isNotFound());

        verify(roleService, times(1)).findById(id);
    }

    @Test
    @DisplayName("POST /api/role - Should create a permission and return 201 Created")
    @WithMockUser(roles = {"ADMIN"})
    public void createRole_ShouldReturnCreated() throws Exception {

        Set<Permission> permissions = new HashSet<>();
        permissions.add(new Permission());
        RoleRequestDTO requestDTO = new RoleRequestDTO("UPDATE",permissions);
        RoleResponseDTO responseDTO = new RoleResponseDTO(2L, "UPDATE",permissions);

        when(roleService.save(any(RoleRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/role")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.role").value("UPDATE"))
                .andExpect(jsonPath("$.permissionSet").isArray())
                .andExpect(jsonPath("$.permissionSet", not(empty())));

        verify(roleService, times(1)).save(any(RoleRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/role - Should return 400 Bad Request when validation fails")
    @WithMockUser(roles = {"ADMIN"})
    public void createRole_ShouldReturnBadRequest_WhenNameIsEmpty() throws Exception {
        // Arrange
        RoleRequestDTO invalidRequest = new RoleRequestDTO("",new HashSet<>());
        // Act & Assert
        mockMvc.perform(post("/api/role")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(roleService, never()).save(any());
    }

    @Test
    @DisplayName("DELETE /api/role/{id} - Should return 24 No Content when successful")
    @WithMockUser(roles = {"ADMIN"})
    public void deleteRole_ShouldReturnNoContent_WhenIdExists() throws Exception {

        Long id = 1L;
        doNothing().when(roleService).deleteById(id);

        mockMvc.perform(delete("/api/role/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(roleService, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/role/{id} - Should return 404 Not Found when ID does not exist")
    @WithMockUser(roles = {"ADMIN"})
    public void deleteRole_ShouldReturnNotFound_WhenIdDoesNotExist() throws Exception {
        // Arrange
        Long id = 99L;
        doThrow(new ResourceNotFoundException("Id '" + id + "' not found."))
                .when(roleService).deleteById(id);

        // Act & Assert
        mockMvc.perform(delete("/api/role/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Id '99' not found."));

        verify(roleService, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("PUT /api/role/{id} - Should return 400 Bad Request when update data is invalid")
    @WithMockUser(roles = {"ADMIN"})
    public void updatePermission_ShouldReturnBadRequest_WhenDataInvalid() throws Exception {
        RoleRequestDTO invalidRequest = new RoleRequestDTO("",new HashSet<>());

        mockMvc.perform(put("/api/role/{id}", 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/role/{id} - Should return 404 Not Found when updating non-existent ID")
    @WithMockUser(roles = {"ADMIN"})
    public void updateRole_ShouldReturnNotFound_WhenIdDoesNotExist() throws Exception {
        Long id = 99L;
        Set<Permission> validPermissions = new HashSet<>();
        validPermissions.add(new Permission());
        RoleRequestDTO requestDTO = new RoleRequestDTO("NEW_NAME",validPermissions);

        when(roleService.updateById(eq(id), any())).thenThrow(new ResourceNotFoundException("Role '\"+ id +\"' not found."));

        mockMvc.perform(put("/api/role/{id}", id)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Role '\"+ id +\"' not found."));
    }

    @Test
    @DisplayName("PUT /api/role/{id} - Should return 200 OK when update is successful")
    @WithMockUser(roles = {"ADMIN"})
    public void updateRole_ShouldReturnOk_WhenDataIsValid() throws Exception {
        // Arrange
        Long id = 1L;
        Set<Permission> validPermissions = new HashSet<>();
        validPermissions.add(new Permission());

        RoleRequestDTO requestDTO = new RoleRequestDTO("ROLE_SUPERUSER", validPermissions);
        RoleResponseDTO responseDTO = new RoleResponseDTO(id, "ROLE_SUPERUSER", validPermissions);

        when(roleService.updateById(eq(id), any(RoleRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/role/{id}", id)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk()) // Generalmente PUT devuelve 200 OK
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.role").value("ROLE_SUPERUSER"));

        verify(roleService, times(1)).updateById(eq(id), any(RoleRequestDTO.class));
    }
}
