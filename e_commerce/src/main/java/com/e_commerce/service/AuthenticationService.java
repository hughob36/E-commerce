package com.e_commerce.service;

import com.e_commerce.dto.AuthLoginRequestDTO;
import com.e_commerce.dto.AuthResponseDTO;
import com.e_commerce.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService{

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponseDTO loginUser(AuthLoginRequestDTO authLoginRequestDTO) {

        String username = authLoginRequestDTO.username();
        String password = authLoginRequestDTO.password();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username,password);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.createToken(authentication);

        return new AuthResponseDTO(username,"Login successfull.",token,true);
    }
}
