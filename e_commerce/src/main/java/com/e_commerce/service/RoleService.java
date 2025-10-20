package com.e_commerce.service;

import com.e_commerce.exception.ResourceNotFoundException;
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

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User '"+ id +"' not found."));
    }

    @Override
    public Role save(Role role) {
        Set<Permission> permissionSet = new HashSet<>();
        for(Permission permission : role.getPermissionSet()) {
            permissionService.findById(permission.getId()).ifPresent(permissionSet::add);
        }
        role.setPermissionSet(permissionSet);
        return roleRepository.save(role);
    }

    @Override
    public void deleteById(Long id) {
        if(!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id '" + id + "' not found.");
        }
        roleRepository.deleteById(id);
    }

    @Override
    public Role updateById(Long id, Role role) {
        Role roleFound = this.findById(id);
        Set<Permission> permissionSet = new HashSet<>();
        for(Permission permission : role.getPermissionSet()) {
            permissionService.findById(permission.getId()).ifPresent(permissionSet::add);
        }
        roleFound.setRole(role.getRole());
        roleFound.setPermissionSet(permissionSet);
        return roleRepository.save(roleFound);
    }
}
