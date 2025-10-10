package com.e_commerce.service;


import com.e_commerce.model.Permission;

import java.util.List;
import java.util.Optional;

public interface IPermissionService {

    public List<Permission> findAll();
    public Optional<Permission> findById(Long id);
    public Permission save(Permission permission);
    public boolean deleteById(Long id);
    public Permission updateById(Long id, Permission permission);
}
