package com.e_commerce.service;



import com.e_commerce.dto.AuthLoginRequestDTO;
import com.e_commerce.dto.AuthResponseDTO;
import com.e_commerce.model.UserApp;
import com.e_commerce.repository.IUserAppRepository;
import com.e_commerce.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiseImpl implements UserDetailsService {

    @Autowired
    private IUserAppRepository userAppRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

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


    public AuthResponseDTO loginUser(AuthLoginRequestDTO authLoginRequestDTO) {

        String username = authLoginRequestDTO.username();
        String password = authLoginRequestDTO.password();

        Authentication authentication = authentication(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.createToken(authentication);

        return new AuthResponseDTO(username,"Login successfull.",token,true);
    }

    public Authentication authentication(String username, String password) {

        UserDetails userDetails = this.loadUserByUsername(username);

        if(userDetails == null) {
            throw new BadCredentialsException("Invalid username or password.");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password.");
        }
        return new UsernamePasswordAuthenticationToken(username,
                        userDetails.getPassword(),userDetails.getAuthorities());
    }
}
