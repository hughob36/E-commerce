package com.e_commerce.controller;

import com.e_commerce.dto.RoleRequestDTO;
import com.e_commerce.dto.RoleResponseDTO;
import com.e_commerce.dto.UserAppRequestDTO;
import com.e_commerce.dto.UserAppResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.model.Permission;
import com.e_commerce.model.Role;
import com.e_commerce.model.UserApp;
import com.e_commerce.service.IRoleService;
import com.e_commerce.service.IUserAppService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserAppController.class)
public class UserAppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IUserAppService userAppService;

    @MockitoBean
    private IRoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/user - Should return list of userApp with 200 OK when data exists")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getAllUserApp_ShouldReturnUserAppResponseDTOList() throws Exception {

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role());
        UserAppResponseDTO userApp = UserAppResponseDTO.builder()
                .id(1L)
                .name("Juan")
                .username("jperez")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .createdAt(LocalDateTime.now())
                .build();
        List<UserAppResponseDTO> userAppList = List.of(userApp);

        when(userAppService.findAll()).thenReturn(userAppList);

        mockMvc.perform(get("/api/user").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Juan"))
                .andExpect(jsonPath("$[0].username").value("jperez"))
                .andExpect(jsonPath("$[0].email").value("juan@mail.com"))
                .andExpect(jsonPath("$[0].password").value("secret"))
                .andExpect(jsonPath("$[0].enable").value(true))
                .andExpect(jsonPath("$[0].accountNotExpired").value(true))
                .andExpect(jsonPath("$[0].accountNotLocked").value(true))
                .andExpect(jsonPath("$[0].credentialNotExpired").value(true))
                .andExpect(jsonPath("$[0].roleSet").isArray())
                .andExpect(jsonPath("$[0].roleSet", not(empty())));

        verify(userAppService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/user - Should return 401 Unauthorized when no user is authenticated")
    public void getAllUser_WithoutUser_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/user/{id} - Should return user details with 200 OK when ID exists")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getUserAppById_ShouldReturnRoleResponseDTO() throws Exception {
        Long id = 1L;
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role());
        UserAppResponseDTO userAppResponseDTO = UserAppResponseDTO.builder()
                .id(id)
                .name("Juan")
                .username("jperez")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .createdAt(LocalDateTime.now())
                .build();
        List<UserAppResponseDTO> userAppList = List.of(userAppResponseDTO);

        when(userAppService.findById(id)).thenReturn(userAppResponseDTO);

        mockMvc.perform(get("/api/user/{id}",id).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Juan"))
                .andExpect(jsonPath("$.username").value("jperez"))
                .andExpect(jsonPath("$.email").value("juan@mail.com"))
                .andExpect(jsonPath("$.password").value("secret"))
                .andExpect(jsonPath("$.enable").value(true))
                .andExpect(jsonPath("$.accountNotExpired").value(true))
                .andExpect(jsonPath("$.accountNotLocked").value(true))
                .andExpect(jsonPath("$.credentialNotExpired").value(true))
                .andExpect(jsonPath("$.roleSet").isArray())
                .andExpect(jsonPath("$.roleSet", not(empty())));

        verify(userAppService, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/user/{id} - Should return 404 Not Found when permission ID does not exist")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getUserAppById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {

        Long id = 99L;

        when(userAppService.findById(id)).thenThrow(new ResourceNotFoundException("User '"+ id +"' not found."));

        mockMvc.perform(get("/api/user/{id}", id))
                .andExpect(status().isNotFound());

        verify(userAppService, times(1)).findById(id);
    }

    @Test
    @DisplayName("POST /api/user - Should create a user and return 201 Created")
    @WithMockUser(roles = {"ADMIN"})
    public void createUser_ShouldReturnCreated() throws Exception {

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role());
        UserAppRequestDTO userAppRequestDTO = UserAppRequestDTO.builder()
                .name("Juan")
                .lastName("Perez")
                .username("jperez")
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

        UserAppResponseDTO userAppResponseDTO = UserAppResponseDTO.builder()
                .id(1L)
                .name("Juan")
                .username("jperez")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .createdAt(LocalDateTime.now())
                .build();

        when(userAppService.save(any(UserAppRequestDTO.class))).thenReturn(userAppResponseDTO);

        mockMvc.perform(post("/api/user")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAppRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Juan"))
                .andExpect(jsonPath("$.username").value("jperez"))
                .andExpect(jsonPath("$.email").value("juan@mail.com"))
                .andExpect(jsonPath("$.password").value("secret"))
                .andExpect(jsonPath("$.enable").value(true))
                .andExpect(jsonPath("$.accountNotExpired").value(true))
                .andExpect(jsonPath("$.accountNotLocked").value(true))
                .andExpect(jsonPath("$.credentialNotExpired").value(true))
                .andExpect(jsonPath("$.roleSet").isArray())
                .andExpect(jsonPath("$.roleSet", not(empty())));

        verify(userAppService, times(1)).save(any(UserAppRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/user - Should return 400 Bad Request when validation fails")
    @WithMockUser(roles = {"ADMIN"})
    public void createUser_ShouldReturnBadRequest_WhenNameIsEmpty() throws Exception {
        // Arrange
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role());
        UserAppRequestDTO invalidRequestDTO = UserAppRequestDTO.builder()
                .name("Juan")
                .lastName("Perez")
                .username("")
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
        // Act & Assert
        mockMvc.perform(post("/api/user")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDTO)))
                .andExpect(status().isBadRequest());

        verify(userAppService, never()).save(any());
    }

    @Test
    @DisplayName("DELETE /api/user/{id} - Should return 204 No Content when successful")
    @WithMockUser(roles = {"ADMIN"})
    public void deleteUser_ShouldReturnNoContent_WhenIdExists() throws Exception {

        Long id = 1L;
        doNothing().when(userAppService).deleteById(id);

        mockMvc.perform(delete("/api/user/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(userAppService, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/user/{id} - Should return 404 Not Found when ID does not exist")
    @WithMockUser(roles = {"ADMIN"})
    public void deleteUser_ShouldReturnNotFound_WhenIdDoesNotExist() throws Exception {
        // Arrange
        Long id = 99L;
        doThrow(new ResourceNotFoundException("User '" + id + "' not found."))
                .when(userAppService).deleteById(id);

        // Act & Assert
        mockMvc.perform(delete("/api/user/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User '"+ id +"' not found."));

        verify(userAppService, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("PUT /api/user/{id} - Should return 400 Bad Request when update data is invalid")
    @WithMockUser(roles = {"ADMIN"})
    public void updateUser_ShouldReturnBadRequest_WhenDataInvalid() throws Exception {

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role());
        UserAppRequestDTO invalidRequestDTO = UserAppRequestDTO.builder()
                .name("Juan")
                .lastName("Perez")
                .username("")
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

        mockMvc.perform(put("/api/user/{id}", 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/user/{id} - Should return 404 Not Found when updating non-existent ID")
    @WithMockUser(roles = {"ADMIN"})
    public void updateUser_ShouldReturnNotFound_WhenIdDoesNotExist() throws Exception {
        Long id = 99L;
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role());
        UserAppRequestDTO requestDTO = UserAppRequestDTO.builder()
                .name("Juan")
                .lastName("Perez")
                .username("jperez")
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

        when(userAppService.updateById(eq(id), any())).thenThrow(new ResourceNotFoundException("User '"+ id +"' not found."));

        mockMvc.perform(put("/api/user/{id}", id)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User '"+ id +"' not found."));
    }

    @Test
    @DisplayName("PUT /api/user/{id} - Should return 200 OK when update is successful")
    @WithMockUser(roles = {"ADMIN"})
    public void updateUser_ShouldReturnOk_WhenDataIsValid() throws Exception {
        // Arrange
        Long id = 1L;
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role());
        UserAppRequestDTO requestDTO = UserAppRequestDTO.builder()
                .name("Juan")
                .lastName("Perez")
                .username("jperez")
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

        UserAppResponseDTO responseDTO = UserAppResponseDTO.builder()
                .id(1L)
                .name("Juan")
                .username("jperez2")
                .email("juan@mail.com")
                .password("secret")
                .enable(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .roleSet(roleSet)
                .createdAt(LocalDateTime.now())
                .build();

        when(userAppService.updateById(eq(id), any(UserAppRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/user/{id}", id)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Juan"))
                .andExpect(jsonPath("$.username").value("jperez2"))
                .andExpect(jsonPath("$.email").value("juan@mail.com"))
                .andExpect(jsonPath("$.password").value("secret"))
                .andExpect(jsonPath("$.enable").value(true))
                .andExpect(jsonPath("$.accountNotExpired").value(true))
                .andExpect(jsonPath("$.accountNotLocked").value(true))
                .andExpect(jsonPath("$.credentialNotExpired").value(true))
                .andExpect(jsonPath("$.roleSet").isArray())
                .andExpect(jsonPath("$.roleSet", not(empty())));

        verify(userAppService, times(1)).updateById(eq(id), any(UserAppRequestDTO.class));
    }

}
