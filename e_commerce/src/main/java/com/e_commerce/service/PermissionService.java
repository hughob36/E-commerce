package com.e_commerce.service;

import com.e_commerce.exception.ResourceNotFoundException;
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

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission findById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id '" + id + "' not found."));
    }

    @Override
    public Optional<Permission> findByIdOptional(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public Permission save(Permission permission) {

        return permissionRepository.save(permission);
    }

    @Override
    public void deleteById(Long id) {
        if(permissionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id '" + id + "' not found.");
        }
        permissionRepository.deleteById(id);
    }

    @Override
    public Permission updateById(Long id, Permission permission) {
        Permission permissionFound = this.findById(id);
        permissionFound.setPermissionName(permission.getPermissionName());
        return permissionRepository.save(permissionFound);
    }
}
