package com.e_commerce.service;


import com.e_commerce.dto.PermissionRequestDTO;
import com.e_commerce.dto.PermissionResponseDTO;
import com.e_commerce.model.Permission;

import java.util.List;
import java.util.Optional;

public interface IPermissionService {

    public List<PermissionResponseDTO> findAll();
    public PermissionResponseDTO findById(Long id);
    public Optional<Permission> findByIdOptional(Long id);
    public PermissionResponseDTO save(PermissionRequestDTO permission);
    public void deleteById(Long id);
    public PermissionResponseDTO updateById(Long id, PermissionRequestDTO permission);
}
