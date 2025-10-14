package com.e_commerce.service;



import com.e_commerce.model.Role;
import com.e_commerce.model.UserApp;
import com.e_commerce.repository.IUserAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiseImpl implements UserDetailsService {

    @Autowired
    private IUserAppRepository userAppRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

           UserApp userApp = userAppRepository.findEntityUserAppByUsername(username)
                   .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' was not found."));


        List<SimpleGrantedAuthority>authorityList = new ArrayList<>();

        userApp.getRoleSet()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));

        userApp.getRoleSet()
                .stream()
                .flatMap(role -> role.getPermissionSet().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName())));

        return new User(
                userApp.getUsername(),
                userApp.getPassword(),
                userApp.isEnable(),
                userApp.isAccountNotExpired(),
                userApp.isCredentialNotExpired(),
                userApp.isAccountNotLocked(),
                authorityList
                );
    }
}
