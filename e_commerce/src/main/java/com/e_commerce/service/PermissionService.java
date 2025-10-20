package com.e_commerce.service;

import com.e_commerce.dto.PermissionRequestDTO;
import com.e_commerce.dto.PermissionResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.IPermissionMapper;
import com.e_commerce.model.Permission;
import com.e_commerce.repository.IPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionService implements IPermissionService{

    private final IPermissionRepository permissionRepository;
    private final IPermissionMapper permissionMapper;

    @Override
    public List<PermissionResponseDTO> findAll() {
        return permissionMapper.toPermissionResponseDTOList(permissionRepository.findAll());
    }

    @Override
    public PermissionResponseDTO findById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id '" + id + "' not found."));
        return permissionMapper.toPermissionResponseDTO(permission);
    }

    @Override
    public Optional<Permission> findByIdOptional(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public PermissionResponseDTO save(PermissionRequestDTO permissionRequestDTO) {
        Permission permission = permissionMapper.toPermission(permissionRequestDTO);
        return permissionMapper.toPermissionResponseDTO(permissionRepository.save(permission));
    }

    @Override
    public void deleteById(Long id) {
        if(!permissionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id '" + id + "' not found.");
        }
        permissionRepository.deleteById(id);
    }

    @Override
    public PermissionResponseDTO updateById(Long id, PermissionRequestDTO permissionRequestDTO) {
        Permission permissionFound = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id '" + id + "' not found."));
        Permission permission = permissionMapper.toPermission(permissionRequestDTO);
        permissionFound.setPermissionName(permission.getPermissionName());
        return permissionMapper.toPermissionResponseDTO( permissionRepository.save(permissionFound));
    }
}
