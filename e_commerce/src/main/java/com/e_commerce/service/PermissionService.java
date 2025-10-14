package com.e_commerce.service;

import com.e_commerce.model.Permission;
import com.e_commerce.repository.IPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService implements IPermissionService{

    @Autowired
    private IPermissionRepository permissionRepository;

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public boolean deleteById(Long id) {

        Permission permission = permissionRepository.findById(id).orElse(null);
        if(permission !=  null) {
            permissionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Permission updateById(Long id, Permission permission) {

        Permission permissionFound = permissionRepository.findById(id).orElse(null);

        if(permissionFound != null) {
            permissionFound.setPermissionName(permission.getPermissionName());
            return permissionRepository.save(permissionFound);
        }
        return permissionFound;
    }
}
