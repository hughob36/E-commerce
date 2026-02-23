package com.e_commerce.service;

import com.e_commerce.dto.RoleResponseDTO;
import com.e_commerce.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    public List<RoleResponseDTO> findAll();
    public RoleResponseDTO findById(Long id);
    public Optional<Role> findByIdOptional(Long id);
    public Role save(Role role);
    public void deleteById(Long id);
    public Role updateById(Long id, Role role);

}
