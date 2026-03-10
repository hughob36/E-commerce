package com.e_commerce.service;


import com.e_commerce.dto.PermissionRequestDTO;
import com.e_commerce.dto.PermissionResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.IPermissionMapper;
import com.e_commerce.model.Permission;
import com.e_commerce.repository.IPermissionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {

    @Mock
    private IPermissionRepository permissionRepository;

    @Mock
    private IPermissionMapper permissionMapper;

    @InjectMocks
    private PermissionService permissionService;

    @Captor
    private ArgumentCaptor<Permission> argumentCaptor;

    @Test
    @DisplayName("Should return a list of PermissionResponseDTO when findAll is called")
    public void findAll_ShouldReturnPermissionResponseDTOList() {
        //Arrange
        Permission permission = new Permission(1L, "CREATE");
        List<Permission> permissionList = List.of(permission);

        PermissionResponseDTO responseDTO = new PermissionResponseDTO(1L, "CREATE");
        List<PermissionResponseDTO> expectedResponse = List.of(responseDTO);

        when(permissionRepository.findAll()).thenReturn(permissionList);
        when(permissionMapper.toPermissionResponseDTOList(permissionList)).thenReturn(expectedResponse);
        //Act
        List<PermissionResponseDTO> result = permissionService.findAll();
        //Assert
        assertAll("Verify response properties",
                () -> assertNotNull(result, "The result should not be null"),
                () -> assertEquals(1, result.size(), "The list size should be 1"),
                () -> assertEquals("CREATE", result.get(0).getPermissionName(), "The permission name does not match")
        );

        verify(permissionRepository, times(1)).findAll();
        verify(permissionMapper, times(1)).toPermissionResponseDTOList(anyList());
        verifyNoMoreInteractions(permissionRepository, permissionMapper);
    }

    @Test
    @DisplayName("Should return an empty list when no permissions exist in the database")
    public void findAll_ShouldReturnEmptyPermissionResponseDTOList() {
        // Arrange
        List<Permission> emptyPermissionList = List.of();
        List<PermissionResponseDTO> emptyResponseDTOList = List.of();

        when(permissionRepository.findAll()).thenReturn(emptyPermissionList);
        when(permissionMapper.toPermissionResponseDTOList(emptyPermissionList)).thenReturn(emptyResponseDTOList);
        // Act
        List<PermissionResponseDTO> result = permissionService.findAll();
        // Assert
        assertAll("Verify empty response properties",
                () -> assertNotNull(result, "The result should not be null even if empty"),
                () -> assertTrue(result.isEmpty(), "The list should be empty"),
                () -> assertEquals(0, result.size(), "The size of the list should be 0")
        );
        // Verificamos que se llamó a los métodos aunque la lista esté vacía
        verify(permissionRepository, times(1)).findAll();
        verify(permissionMapper, times(1)).toPermissionResponseDTOList(emptyPermissionList);
    }

    @Test
    @DisplayName("Should return a PermissionResponseDTO when findById is called")
    public void findByID_ShouldReturnPermissionResponseDTO() {
        Long id = 1L;
        Permission permission = new Permission(id,"CREATE");
        PermissionResponseDTO permissionResponseDTO = new PermissionResponseDTO(id,"CREATE");

        when(permissionRepository.findById(id)).thenReturn(Optional.of(permission));
        when(permissionMapper.toPermissionResponseDTO(permission)).thenReturn(permissionResponseDTO);

        PermissionResponseDTO result = permissionService.findById(id);

        assertAll("Verify response properties",
                () -> assertNotNull(result, "The result should not be null"),
                () -> assertEquals("CREATE", result.getPermissionName(), "The permission name does not match"),
                () -> assertEquals(id, result.getId(), "The ID does not match")
        );
        verify(permissionRepository, times(1)).findById(id);
        verify(permissionMapper, times(1)).toPermissionResponseDTO(any());
        verifyNoMoreInteractions(permissionRepository, permissionMapper);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID does not exist")
    public void findById_ShouldThrowResourceNotFoundException_WhenIdDoesNotExist() {
        // Arrange
        Long id = 1L;
        when(permissionRepository.findById(id)).thenReturn(Optional.empty());
        // Act & Assert
        // assertThrows guarda la excepción lanzada para que podamos analizarla
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            permissionService.findById(id);
        });
        // Verificamos que el mensaje de la excepción sea el esperado
        assertEquals("Id '1' not found.", exception.getMessage());
        // Verificaciones de comportamiento
        verify(permissionRepository, times(1)).findById(id);
        // IMPORTANTE: El mapper NUNCA debería ejecutarse si el ID no existe
        verifyNoInteractions(permissionMapper);
    }

    @Test
    @DisplayName("Should save a permission and return its DTO")
    public void save_ShouldReturnPermissionResponseDTO_WhenSuccessful() {
        // 1. Arrange
        PermissionRequestDTO requestDTO = new PermissionRequestDTO("CREATE");
        Permission permissionWithoutId = new Permission(null, "CREATE"); // Como es nuevo, no tiene ID
        Permission savedPermission = new Permission(1L, "CREATE");       // La BD le asigna el ID 1
        PermissionResponseDTO expectedResponse = new PermissionResponseDTO(1L, "CREATE");
        // Definimos el comportamiento de los mocks
        when(permissionMapper.toPermission(requestDTO)).thenReturn(permissionWithoutId);
        when(permissionRepository.save(permissionWithoutId)).thenReturn(savedPermission);
        when(permissionMapper.toPermissionResponseDTO(savedPermission)).thenReturn(expectedResponse);
        // 2. Act
        PermissionResponseDTO result = permissionService.save(requestDTO);
        // 3. Assert
        assertAll("Verify save process",
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("CREATE", result.getPermissionName())
        );

        verify(permissionRepository, times(1)).save(argumentCaptor.capture());
        Permission capturedPermission = argumentCaptor.getValue();
        // Comprobamos que lo que le llegó al repositorio sea lo que el mapper transformó
        assertEquals("CREATE", capturedPermission.getPermissionName(), "The name sent to repo is wrong");

        verify(permissionMapper).toPermission(any());
        verify(permissionMapper).toPermissionResponseDTO(any());
    }

    @Test
    @DisplayName("Should throw DataIntegrityViolationException when permission name already exists")
    public void save_ShouldThrowDataIntegrityViolationException_WhenNameExists() {
        // Arrange
        PermissionRequestDTO requestDTO = new PermissionRequestDTO("CREATE");
        Permission permission = new Permission(null, "CREATE");

        when(permissionMapper.toPermission(requestDTO)).thenReturn(permission);
        // Simulamos que el repositorio lanza la excepción de integridad (duplicado)
        when(permissionRepository.save(permission))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry 'CREATE' for key 'permission_name'"));

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            permissionService.save(requestDTO);
        });

        verify(permissionRepository, times(1)).save(permission);
        // El mapper para el DTO de respuesta NUNCA debe ejecutarse porque el proceso falló antes
        verify(permissionMapper, never()).toPermissionResponseDTO(any());
    }

    @Test
    @DisplayName("Should delete a permission when ID exists")
    public void deleteById_ShouldDeletePermission_WhenIdExists() {
        // Arrange
        Long id = 1L;
        // Simulamos que el permiso EXISTE para que pase el if
        when(permissionRepository.existsById(id)).thenReturn(true);
        // Act
        permissionService.deleteById(id);
        // Assert
        verify(permissionRepository, times(1)).existsById(id);
        verify(permissionRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent ID")
    public void deleteById_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        Long id = 1L;

        when(permissionRepository.existsById(id)).thenReturn(false);
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> permissionService.deleteById(id));

        // Verificamos que intentó buscarlo pero NUNCA llamó al método deleteById
        verify(permissionRepository, times(1)).existsById(id);
        verify(permissionRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should update and return DTO when ID exists")
    public void updateById_ShouldReturnUpdatedDTO_WhenIdExists() {
        // 1. Arrange
        Long id = 1L;
        PermissionRequestDTO requestDTO = new PermissionRequestDTO("NEW_NAME");
        Permission existingPermission = new Permission(id, "OLD_NAME");
        Permission updatedPermission = new Permission(id, "NEW_NAME");
        PermissionResponseDTO expectedResponse = new PermissionResponseDTO(id, "NEW_NAME");

        when(permissionRepository.findById(id)).thenReturn(Optional.of(existingPermission));
        // El mapper actualiza el objeto 'existingPermission' directamente
        doNothing().when(permissionMapper).updatePermissionFromDto(requestDTO, existingPermission);
        when(permissionRepository.save(existingPermission)).thenReturn(updatedPermission);
        when(permissionMapper.toPermissionResponseDTO(updatedPermission)).thenReturn(expectedResponse);

        // 2. Act
        PermissionResponseDTO result = permissionService.updateById(id, requestDTO);

        // 3. Assert (Verificación de resultados)
        assertAll("Verify update results",
                () -> assertNotNull(result),
                () -> assertEquals("NEW_NAME", result.getPermissionName()),
                () -> assertEquals(id, result.getId())
        );
        // 4. Verification (Verificación de ORDEN de ejecución)
        // Creamos el verificador de orden para los mocks involucrados
        org.mockito.InOrder inOrder = inOrder(permissionRepository, permissionMapper);

        // Verificamos que se ejecuten en la secuencia exacta de tu código
        inOrder.verify(permissionRepository).findById(id);                                  // Paso 1: Buscar
        inOrder.verify(permissionMapper).updatePermissionFromDto(requestDTO, existingPermission); // Paso 2: Mapear datos nuevos
        inOrder.verify(permissionRepository).save(existingPermission);                       // Paso 3: Guardar cambios
        inOrder.verify(permissionMapper).toPermissionResponseDTO(updatedPermission);         // Paso 4: Convertir a respuesta

        // Finalmente, aseguramos que no hubo llamadas extrañas fuera de este orden
        verifyNoMoreInteractions(permissionRepository, permissionMapper);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent ID")
    public void updateById_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        Long id = 1L;
        PermissionRequestDTO requestDTO = new PermissionRequestDTO("NEW_NAME");
        when(permissionRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> permissionService.updateById(id, requestDTO));

        // Verificamos que el proceso se detuvo tras no encontrar el ID
        verify(permissionRepository).findById(id);
        verify(permissionRepository, never()).save(any());
        verifyNoInteractions(permissionMapper); // El mapper no debería tocarse
    }

}
