package com.e_commerce.service;

import com.e_commerce.dto.RoleRequestDTO;
import com.e_commerce.dto.RoleResponseDTO;
import com.e_commerce.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    public List<RoleResponseDTO> findAll();
    public RoleResponseDTO findById(Long id);
    public Optional<Role> findByIdOptional(Long id);
    public RoleResponseDTO save(RoleRequestDTO roleRequestDTO);
    public void deleteById(Long id);
    public RoleResponseDTO updateById(Long id, RoleRequestDTO roleRequestDTO);

}
