package com.e_commerce.service;



import com.e_commerce.dto.RoleRequestDTO;
import com.e_commerce.dto.UserAppRequestDTO;
import com.e_commerce.dto.UserAppResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.IUserAppMapper;
import com.e_commerce.model.Role;
import com.e_commerce.model.UserApp;
import com.e_commerce.repository.IUserAppRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class UserAppServiceTest {

    @Mock
    private IUserAppRepository userAppRepository;

    @Mock
    private IUserAppMapper userAppMapper;

    @Mock
    private IRoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserAppService userAppService;

    @Captor
    private ArgumentCaptor<UserApp> argumentCaptor;

    @Test
    @DisplayName("Should return a list of UserAppResponseDTO when findAll is called")
    public void findAll_shouldReturnUserResponseDTOList() {

        Set<Role> roleSet = new HashSet<>();
        // En tu clase añade: @Builder
        UserApp userApp = UserApp.builder()
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
        List<UserApp> userAppList = List.of(userApp);

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
        List<UserAppResponseDTO> userAppResponseDTOList = List.of(userAppResponseDTO);

        when(userAppRepository.findAll()).thenReturn(userAppList);
        when(userAppMapper.toUserAppResponseDTOList(userAppList)).thenReturn(userAppResponseDTOList);

        List<UserAppResponseDTO> result = userAppService.findAll();

        assertAll("Verify response properties",
                () -> assertNotNull(result, "The result should not be null"),
                () -> assertEquals(1, result.size(), "The list size should be 1"),
                () -> assertEquals("jperez", result.get(0).getUsername(), "The role name does not match")
        );
        verify(userAppRepository, times(1)).findAll();
        verify(userAppMapper, times(1)).toUserAppResponseDTOList(anyList());
        verifyNoMoreInteractions(userAppRepository,userAppMapper);
    }

    @Test
    @DisplayName("Should return an empty list when no userApp exist in the database")
    public void findAll_ShouldReturnEmptyUserAppResponseDTOList() {

        List<UserApp> emptyUserAppList = List.of();
        List<UserAppResponseDTO> emptyResponseDTOList = List.of();

        when(userAppRepository.findAll()).thenReturn(emptyUserAppList);
        when(userAppMapper.toUserAppResponseDTOList(emptyUserAppList)).thenReturn(emptyResponseDTOList);

        List<UserAppResponseDTO> result = userAppService.findAll();

        assertAll("Verify empty response properties",
                () -> assertNotNull(result, "The result should not be null even if empty"),
                () -> assertTrue(result.isEmpty(), "The list should be empty"),
                () -> assertEquals(0, result.size(), "The size of the list should be 0")
        );
        verify(userAppRepository, times(1)).findAll();
        verify(userAppMapper, times(1)).toUserAppResponseDTOList(emptyUserAppList);
    }

    @Test
    @DisplayName("Should return a UserAppResponseDTO when findById is called")
    public void findByID_ShouldReturnUserAppResponseDTO() {

        Long id = 1L;
        Set<Role> roleSet = new HashSet<>();
        UserApp userApp = UserApp.builder()
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

        when(userAppRepository.findById(id)).thenReturn(Optional.of(userApp));
        when(userAppMapper.toUserAppResponseDTO(userApp)).thenReturn(userAppResponseDTO);

        UserAppResponseDTO result = userAppService.findById(id);

        assertAll("Verify response properties",
                () -> assertNotNull(result, "The result should not be null"),
                () -> assertEquals("jperez", result.getUsername(), "The role name does not match"),
                () -> assertEquals(id, result.getId(), "The ID does not match")
        );
        verify(userAppRepository, times(1)).findById(id);
        verify(userAppMapper, times(1)).toUserAppResponseDTO(any());
        verifyNoMoreInteractions(userAppRepository,userAppMapper);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID does not exist")
    public void findByID_ShouldThrowResourceNotFoundException_WhenIdDoesNotExist() {

        Long id = 1L;
        when(userAppRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userAppService.findById(id));

        assertEquals("User '1' not found.", exception.getMessage());
        verify(userAppRepository, times(1)).findById(id);
        verifyNoInteractions(userAppMapper);
    }

    @Test
    @DisplayName("Should save a user with roles and return its DTO")
    public void save_ShouldReturnUserAppResponseDTO_WhenSuccessful() {
        // --- Arrange ---
        Long id = 1L;
        String rawPassword = "secret";
        String encodedPassword = "encoded_secret";

        Role role = new Role(10L, "ADMIN", new HashSet<>());
        Set<Role> roleSet = new HashSet<>(Collections.singletonList(role));

        UserAppRequestDTO requestDTO = UserAppRequestDTO.builder()
                .username("jperez")
                .password(rawPassword)
                .roleSet(roleSet)
                .build();

        // Configuramos el Mapper para que devuelva una ENTIDAD con la pass original
        UserApp entityFromMapper = new UserApp();
        entityFromMapper.setUsername("jperez");
        entityFromMapper.setPassword(rawPassword);

        UserApp savedUser = new UserApp();
        savedUser.setId(id);
        savedUser.setPassword(encodedPassword);

        // --- Mocks ---
        // Importante: Usamos la clave real "secret" para el mock
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        when(userAppMapper.toUserApp(any())).thenReturn(entityFromMapper);
        when(roleService.findByIdOptional(10L)).thenReturn(Optional.of(role));

        // El repositorio devuelve el objeto que ya tiene el ID
        when(userAppRepository.save(any(UserApp.class))).thenReturn(savedUser);
        when(userAppMapper.toUserAppResponseDTO(any())).thenReturn(new UserAppResponseDTO());

        // --- Act ---
        userAppService.save(requestDTO);

        // --- Assert ---
        verify(userAppRepository).save(argumentCaptor.capture());
        UserApp captured = argumentCaptor.getValue();
    }

    @Test
    @DisplayName("Should throw DataIntegrityViolationException when userApp name already exists")
    public void save_ShouldThrowException_WhenRoleNameExists() {

        String rawPassword = "secret";
        String encodedPassword = "encoded_secret";

        Role role = new Role(10L, "ADMIN", new HashSet<>());
        Set<Role> roleSet = new HashSet<>(Collections.singletonList(role));

        UserAppRequestDTO requestDTO = UserAppRequestDTO.builder()
                .username("jperez")
                .password(rawPassword)
                .roleSet(roleSet)
                .build();

        UserApp entityFromMapper = new UserApp();
        entityFromMapper.setUsername("jperez");
        entityFromMapper.setPassword(rawPassword);

        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn(encodedPassword);

        when(userAppMapper.toUserApp(requestDTO)).thenReturn(entityFromMapper);

        when(userAppRepository.save(any(UserApp.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry 'ADM' for key 'Users.username'"));

        assertThrows(DataIntegrityViolationException.class, () -> {
            userAppService.save(requestDTO);
        });

        verify(userAppRepository, times(1)).save(any(UserApp.class));
        verify(userAppMapper, never()).toUserAppResponseDTO(any());
    }

    @Test
    @DisplayName("Should delete a userApp when ID exists")
    public void deleteById_ShouldDeleteUserApp_WhenIdExists() {
        Long id = 1L;
        when(userAppRepository.existsById(id)).thenReturn(true);

        userAppService.deleteById(id);

        verify(userAppRepository, times(1)).existsById(id);
        verify(userAppRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent ID")
    public void deleteById_ShouldThrowException_WhenIdDoesNotExist() {
        Long id = 1L;
        when(userAppRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userAppService.deleteById(id));

        verify(userAppRepository, times(1)).existsById(id);
        verify(userAppRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should update and return DTO when ID exists")
    public void updateById_ShouldReturnUpdatedDTO_WhenIdExists() {
        // 1. ARRANGE
        Long id = 1L;
        String rawPassword = "secret";
        String encodedPassword = "encoded_secret";

        Role role = new Role(10L, "ADMIN", new HashSet<>());
        Set<Role> roleSet = new HashSet<>(Collections.singletonList(role));

        UserAppRequestDTO requestDTO = UserAppRequestDTO.builder()
                .username("new_username")
                .password(rawPassword)
                .roleSet(roleSet)
                .build();

        UserApp existingUser = UserApp.builder()
                .id(id)
                .username("old_username")
                .password("old_encoded_pass")
                .roleSet(new HashSet<>())
                .build();

        UserApp updatedUser = UserApp.builder()
                .id(id)
                .username("new_username")
                .password(encodedPassword)
                .roleSet(roleSet)
                .build();

        UserAppResponseDTO expectedResponseUser = UserAppResponseDTO.builder()
                .id(id)
                .username("new_username")
                .roleSet(roleSet)
                .build();

        // Stubbings
        when(userAppRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // IMPORTANTE: Stubbing para el roleService que usa tu bucle for
        when(roleService.findByIdOptional(10L)).thenReturn(Optional.of(role));

        doNothing().when(userAppMapper).updateUserAppFromDTO(requestDTO, existingUser);
        when(userAppRepository.save(existingUser)).thenReturn(updatedUser);
        when(userAppMapper.toUserAppResponseDTO(updatedUser)).thenReturn(expectedResponseUser);

        // 2. ACT
        UserAppResponseDTO result = userAppService.updateById(id, requestDTO);

        // 3. ASSERT
        assertAll("Verify update results",
                () -> assertNotNull(result),
                () -> assertEquals(expectedResponseUser.getUsername(), result.getUsername()),
                () -> assertEquals(id, result.getId())
        );

        verify(userAppRepository).findById(id);
        verify(passwordEncoder).encode(rawPassword);
        verify(roleService).findByIdOptional(10L);
        verify(userAppMapper).updateUserAppFromDTO(requestDTO, existingUser);
        verify(userAppRepository).save(existingUser);

        // Solo mocks aquí, NUNCA el service bajo prueba
        verifyNoMoreInteractions(userAppRepository, userAppMapper, passwordEncoder, roleService);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID does not exist")
    public void updateById_ShouldThrowException_WhenIdDoesNotExist() {

        Long id = 99L;
        String rawPassword = "secret";
        String encodedPassword = "encoded_secret";

        Role role = new Role(10L, "ADMIN", new HashSet<>());
        Set<Role> roleSet = new HashSet<>(Collections.singletonList(role));

        UserAppRequestDTO requestDTO = UserAppRequestDTO.builder()
                .username("new_username")
                .password(rawPassword)
                .roleSet(roleSet)
                .build();

        when(userAppRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userAppService.updateById(id, requestDTO);
        });

        assertEquals("User '" + id + "' not found.", exception.getMessage());

        verify(userAppRepository, times(1)).findById(id);

        verify(roleService, never()).findByIdOptional(anyLong());
        verify(userAppMapper, never()).updateUserAppFromDTO(any(), any());
        verify(userAppRepository, never()).save(any());

        verifyNoMoreInteractions(userAppRepository, userAppMapper, roleService);
    }

}
