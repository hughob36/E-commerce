package com.e_commerce.service;


import com.e_commerce.dto.PermissionRequestDTO;
import com.e_commerce.dto.PermissionResponseDTO;
import com.e_commerce.dto.RoleRequestDTO;
import com.e_commerce.dto.RoleResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.IRoleMapper;
import com.e_commerce.model.Permission;
import com.e_commerce.model.Role;
import com.e_commerce.repository.IRoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private IRoleMapper roleMapper;

    @Mock
    private IPermissionService permissionService;

    @InjectMocks
    private RoleService roleService;

    @Captor
    private ArgumentCaptor<Role> argumentCaptor;

    @Test
    @DisplayName("Should return a list of RoleResponseDTO when findAll is called")
    public void findAll_shouldReturnRoleResponseDTOList() {

        Set<Permission> permissionSet = new HashSet<>();
        Role role = new Role(1L,"ADM",permissionSet);
        List<Role> roleList = List.of(role);

        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L,"ADM",permissionSet);
        List<RoleResponseDTO> roleResponseDTOList = List.of(roleResponseDTO);

        when(roleRepository.findAll()).thenReturn(roleList);
        when(roleMapper.toRoleResponseDTOList(roleList)).thenReturn(roleResponseDTOList);

        List<RoleResponseDTO> result = roleService.findAll();

        assertAll("Verify response properties",
                () -> assertNotNull(result, "The result should not be null"),
                () -> assertEquals(1, result.size(), "The list size should be 1"),
                () -> assertEquals("ADM", result.get(0).getRole(), "The role name does not match")
        );
        verify(roleRepository, times(1)).findAll();
        verify(roleMapper, times(1)).toRoleResponseDTOList(anyList());
        verifyNoMoreInteractions(roleRepository,roleMapper);
    }


    @Test
    @DisplayName("Should return an empty list when no roles exist in the database")
    public void findAll_ShouldReturnEmptyRoleResponseDTOList() {

        List<Role> emptyRoleList = List.of();
        List<RoleResponseDTO> emptyResponseDTOList = List.of();

        when(roleRepository.findAll()).thenReturn(emptyRoleList);
        when(roleMapper.toRoleResponseDTOList(emptyRoleList)).thenReturn(emptyResponseDTOList);

        List<RoleResponseDTO> result = roleService.findAll();

        assertAll("Verify empty response properties",
                () -> assertNotNull(result, "The result should not be null even if empty"),
                () -> assertTrue(result.isEmpty(), "The list should be empty"),
                () -> assertEquals(0, result.size(), "The size of the list should be 0")
        );
        verify(roleRepository, times(1)).findAll();
        verify(roleMapper, times(1)).toRoleResponseDTOList(emptyRoleList);
    }

    @Test
    @DisplayName("Should return a RoleResponseDTO when findById is called")
    public void findByID_ShouldReturnRoleResponseDTO() {

        Long id = 1L;
        Set<Permission> permissionSet = new HashSet<>();
        Role role = new Role(id,"ADM",permissionSet);
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(id,"ADM",permissionSet);

        when(roleRepository.findById(id)).thenReturn(Optional.of(role));
        when(roleMapper.toRoleResponseDTO(role)).thenReturn(roleResponseDTO);

        RoleResponseDTO result = roleService.findById(id);

        assertAll("Verify response properties",
                () -> assertNotNull(result, "The result should not be null"),
                () -> assertEquals("ADM", result.getRole(), "The role name does not match"),
                () -> assertEquals(id, result.getId(), "The ID does not match")
        );
        verify(roleRepository, times(1)).findById(id);
        verify(roleMapper, times(1)).toRoleResponseDTO(any());
        verifyNoMoreInteractions(roleRepository,roleMapper);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID does not exist")
    public void findByID_ShouldThrowResourceNotFoundException_WhenIdDoesNotExist() {

        Long id = 1L;
        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> roleService.findById(id));

        assertEquals("Role '1' not found.", exception.getMessage());
        verify(roleRepository, times(1)).findById(id);
        verifyNoInteractions(roleMapper);
    }

    @Test
    @DisplayName("Should save a role with permissions and return its DTO")
    public void save_ShouldReturnRoleResponseDTO_WhenSuccessful() {
        // 1. Arrange mejorado con datos
        Long id = 1L;
        Permission permission = new Permission(10L, "READ"); // Un permiso de ejemplo
        Set<Permission> permissions = new HashSet<>(Collections.singletonList(permission));

        RoleRequestDTO requestDTO = new RoleRequestDTO("ADM", permissions);
        Role roleWithoutId = new Role(null, "ADM", permissions);
        Role savedRole = new Role(id, "ADM", permissions);
        RoleResponseDTO expectedResponse = new RoleResponseDTO(id, "ADM", permissions);

        // Mocks
        when(roleMapper.toRole(requestDTO)).thenReturn(roleWithoutId);
        // IMPORTANTE: Mockear la búsqueda del permiso que hace tu lógica
        when(permissionService.findByIdOptional(10L)).thenReturn(Optional.of(permission));
        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);
        when(roleMapper.toRoleResponseDTO(savedRole)).thenReturn(expectedResponse);
        // 2. Act
        RoleResponseDTO result = roleService.save(requestDTO);
        // 3. Assert
        assertAll("Verify response",
                () -> assertNotNull(result),
                () -> assertEquals("ADM", result.getRole()),
                () -> assertEquals(id, result.getId())
        );
        // 4. Verify detallado
        verify(permissionService, times(1)).findByIdOptional(10L); // Verifica que buscó el permiso
        verify(roleRepository).save(argumentCaptor.capture());

        Role captured = argumentCaptor.getValue();
        assertEquals("ADM", captured.getRole());
        // Verifica que el set de permisos procesado no esté vacío
        assertFalse(captured.getPermissionSet().isEmpty());
    }

    @Test
    @DisplayName("Should throw DataIntegrityViolationException when role name already exists")
    public void save_ShouldThrowException_WhenRoleNameExists() {
        // 1. Arrange (Preparar datos)
        RoleRequestDTO requestDTO = new RoleRequestDTO("ADM", new HashSet<>());
        Role roleToSave = new Role(null, "ADM", new HashSet<>());
        // Mockeamos el mapeo inicial
        when(roleMapper.toRole(requestDTO)).thenReturn(roleToSave);
        // Simulamos que el repositorio lanza la excepción de integridad (ej: Unique Constraint Violated)
        when(roleRepository.save(any(Role.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry 'ADM' for key 'roles.role'"));

        // 2. Act & Assert (Actuar y Verificar)
        // Verificamos que al llamar al servicio, este propague la excepción
        assertThrows(DataIntegrityViolationException.class, () -> {
            roleService.save(requestDTO);
        });
        // 3. Verificaciones adicionales
        // Confirmamos que el repositorio se llamó pero falló
        verify(roleRepository, times(1)).save(any(Role.class));
        // El mapeo a DTO de respuesta NUNCA debería ocurrir porque el proceso falló antes
        verify(roleMapper, never()).toRoleResponseDTO(any());
    }

    @Test
    @DisplayName("Should delete a role when ID exists")
    public void deleteById_ShouldDeleteRole_WhenIdExists() {
        Long id = 1L;
        when(roleRepository.existsById(id)).thenReturn(true);

        roleService.deleteById(id);

        verify(roleRepository, times(1)).existsById(id);
        verify(roleRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent ID")
    public void deleteById_ShouldThrowException_WhenIdDoesNotExist() {
        Long id = 1L;
        when(roleRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> roleService.deleteById(id));

        verify(roleRepository, times(1)).existsById(id);
        verify(roleRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should update and return DTO when ID exists")
    public void updateById_ShouldReturnUpdatedDTO_WhenIdExists() {
        Long id = 1L;
        RoleRequestDTO requestDTO = new RoleRequestDTO("NEW_NAME", new HashSet<>());
        Role existingRole = new Role(id,"OLD_NAME",new HashSet<>());
        Role updatedRole = new Role(id,"NEW_NAME",new HashSet<>());
        RoleResponseDTO expectedResponse = new RoleResponseDTO(id, "NEW_NAME", new HashSet<>());

        // Configuración de los Mocks (DEBE IR ANTES DEL ACT)
        when(roleRepository.findById(id)).thenReturn(Optional.of(existingRole));
        // El mapper suele ser un void o actualiza la referencia, doNothing() es correcto para métodos void
        doNothing().when(roleMapper).updateRoleFromDTO(requestDTO, existingRole);
        when(roleRepository.save(existingRole)).thenReturn(updatedRole);
        when(roleMapper.toRoleResponseDTO(updatedRole)).thenReturn(expectedResponse);

        // 2. ACT
        RoleResponseDTO result = roleService.updateById(id, requestDTO);

        // 3. ASSERT
        assertAll("Verify role update results",
                () -> assertNotNull(result, "The result should not be null"),
                () -> assertEquals(expectedResponse.getRole(), result.getRole(), "The role name should match the updated value"),
                () -> assertEquals(id, result.getId(), "The ID should remain the same")
        );
        verify(roleRepository, times(1)).findById(id);
        verify(roleMapper, times(1)).updateRoleFromDTO(requestDTO, existingRole);
        verify(roleRepository, times(1)).save(existingRole);
        verifyNoMoreInteractions(roleRepository, roleMapper, permissionService);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID does not exist")
    public void updateById_ShouldThrowException_WhenIdDoesNotExist() {
        // 1. GIVEN
        Long id = 99L;
        RoleRequestDTO requestDTO = new RoleRequestDTO("ADMIN", new HashSet<>());

        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            roleService.updateById(id, requestDTO);
        });

        assertEquals("User '" + id + "' not found.", exception.getMessage());

        verify(roleRepository, times(1)).findById(id);

        // Crucial: Ensure these methods were NEVER called
        verify(permissionService, never()).findByIdOptional(anyLong());
        verify(roleMapper, never()).updateRoleFromDTO(any(), any());
        verify(roleRepository, never()).save(any());

        verifyNoMoreInteractions(roleRepository, roleMapper, permissionService);
    }

}
