package com.e_commerce.service;

import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.model.Role;
import com.e_commerce.model.UserApp;
import com.e_commerce.repository.IUserAppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserAppService implements IUserAppService{

    private final IUserAppRepository userAppRepository;
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserApp> findAll() {
        return userAppRepository.findAll();
    }

    @Override
    public UserApp findById(Long id) {
        return userAppRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("User '"+ id +"' not found."));
    }

    @Override
    public UserApp save(UserApp userApp) {

        if(userApp.getPassword() != null && !userApp.getPassword().isEmpty()) {
            userApp.setPassword(passwordEncoder.encode(userApp.getPassword()));
        }
        Set<Role> roleSet = new HashSet<>();
        for(Role role : userApp.getRoleSet()) {
            roleService.findById(role.getId()).ifPresent(roleSet::add);
        }
        userApp.setRoleSet(roleSet);
        return userAppRepository.save(userApp);
    }

    @Override
    public void deleteById(Long id) {
       if(!userAppRepository.existsById(id)) {
           throw new ResourceNotFoundException("User '" + id + "' not found.");
       }
        userAppRepository.deleteById(id);
    }

    @Override
    public UserApp updateById(Long id, UserApp userApp) {

        UserApp userAppFound = this.findById(id);

        if(userApp.getPassword() != null && !userApp.getPassword().isEmpty()) {
            userAppFound.setPassword(passwordEncoder.encode(userApp.getPassword()));
        }

        Set<Role> roleSet = new HashSet<>();
        for(Role role : userApp.getRoleSet()) {
            roleService.findById(role.getId()).ifPresent(roleSet::add);
        }
        userAppFound.setName(userApp.getName());
        userAppFound.setLatsName(userApp.getLatsName());
        userAppFound.setUsername(userApp.getUsername());
        userAppFound.setEmail(userApp.getEmail());
        userAppFound.setRoleSet(userApp.getRoleSet());
        userAppFound.setPhone(userApp.getPhone());
        userAppFound.setAddress(userApp.getAddress());
        userAppFound.setCity(userApp.getCity());
        userAppFound.setState(userApp.getState());
        userAppFound.setPostalCode(userApp.getPostalCode());
        userAppFound.setCountry(userApp.getCountry());
        userAppFound.setCreatedAt(userApp.getCreatedAt());
        userAppFound.setUpdatedAt(userApp.getUpdatedAt());
        return userAppRepository.save(userAppFound);
    }
}
