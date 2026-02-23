package com.e_commerce.service;

import com.e_commerce.dto.RoleRequestDTO;
import com.e_commerce.dto.RoleResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.IRoleMapper;
import com.e_commerce.model.Permission;
import com.e_commerce.model.Role;
import com.e_commerce.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{

    private final IRoleRepository roleRepository;
    private final IPermissionService permissionService;
    private final IRoleMapper roleMapper;

    @Override
    public List<RoleResponseDTO> findAll() {
        return roleMapper.toRoleResponseDTOList(roleRepository.findAll());
    }

    @Override
    public RoleResponseDTO findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User '"+ id +"' not found."));
        return roleMapper.toRoleResponseDTO(role);
    }

    @Override
    public Optional<Role> findByIdOptional(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public RoleResponseDTO save(RoleRequestDTO roleRequestDTO) {
        Role role = roleMapper.toRole(roleRequestDTO);
        Set<Permission> permissionSet = new HashSet<>();
        for(Permission permission : role.getPermissionSet()) {
            permissionService.findByIdOptional(permission.getId()).ifPresent(permissionSet::add);
        }
        role.setPermissionSet(permissionSet);
        return roleMapper.toRoleResponseDTO(roleRepository.save(role));
    }

    @Override
    public void deleteById(Long id) {
        if(!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id '" + id + "' not found.");
        }
        roleRepository.deleteById(id);
    }

    @Override
    public RoleResponseDTO updateById(Long id, RoleRequestDTO roleRequestDTO) {
        Role roleFound = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User '"+ id +"' not found."));

        Set<Permission> permissionSet = new HashSet<>();
        for(Permission permission : roleRequestDTO.getPermissionSet()) {
            permissionService.findByIdOptional(permission.getId()).ifPresent(permissionSet::add);
        }
        roleMapper.updateRoleFromDTO(roleRequestDTO,roleFound);
        return roleMapper.toRoleResponseDTO(roleRepository.save(roleFound));
    }
}
