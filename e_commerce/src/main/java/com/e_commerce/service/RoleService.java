package com.e_commerce.service;

import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.model.Role;
import com.e_commerce.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{

    private final IRoleRepository roleRepository;

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
        return roleRepository.save(role);
    }

    @Override
    public boolean deleteById(Long id) {

        Role role = roleRepository.findById(id).orElse(null);
        if(role != null) {
            roleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Role updateById(Long id, Role role) {

        Role roleFound = roleRepository.findById(id).orElse(null);
        if(roleFound != null) {
            roleFound.setRole(role.getRole());
            roleFound.setPermissionSet(role.getPermissionSet());
            return roleRepository.save(roleFound);
        }
        return roleFound;
    }
}
