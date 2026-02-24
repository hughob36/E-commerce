package com.e_commerce.service;


import com.e_commerce.dto.UserAppRequestDTO;
import com.e_commerce.dto.UserAppResponseDTO;
import com.e_commerce.model.UserApp;

import java.util.List;
import java.util.Optional;


public interface IUserAppService {

    public List<UserAppResponseDTO> findAll();
    public UserAppResponseDTO findById(Long id);
    public UserAppResponseDTO save(UserAppRequestDTO userAppRequestDTO);
    public void deleteById(Long id);
    public UserAppResponseDTO updateById(Long id, UserAppRequestDTO userAppRequestDTO);
}
