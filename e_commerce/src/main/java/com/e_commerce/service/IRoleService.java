package com.e_commerce.service;

import com.e_commerce.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    public List<Role> findAll();
    public Role findById(Long id);
    public Role save(Role role);
    public void deleteById(Long id);
    public Role updateById(Long id, Role role);

}
