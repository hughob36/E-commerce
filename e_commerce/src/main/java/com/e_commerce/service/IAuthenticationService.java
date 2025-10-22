package com.e_commerce.service;

import com.e_commerce.dto.AuthLoginRequestDTO;
import com.e_commerce.dto.AuthResponseDTO;

public interface IAuthenticationService {

    public AuthResponseDTO loginUser(AuthLoginRequestDTO authLoginRequestDTO);
}
