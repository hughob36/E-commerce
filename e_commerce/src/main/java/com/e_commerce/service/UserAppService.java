package com.e_commerce.service;

import com.e_commerce.dto.UserAppRequestDTO;
import com.e_commerce.dto.UserAppResponseDTO;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.mapper.IUserAppMapper;
import com.e_commerce.model.Role;
import com.e_commerce.model.UserApp;
import com.e_commerce.repository.IUserAppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserAppService implements IUserAppService{

    private final IUserAppRepository userAppRepository;
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final IUserAppMapper userAppMapper;

    @Override
    public List<UserAppResponseDTO> findAll() {
        return userAppMapper.toUserAppResponseDTOList(userAppRepository.findAll());
    }

    @Override
    public UserAppResponseDTO findById(Long id) {
        UserApp userApp = userAppRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User '"+ id +"' not found."));
        return userAppMapper.toUserAppResponseDTO(userApp);
    }

    @Override
    public UserAppResponseDTO save(UserAppRequestDTO userAppRequestDTO) {

        if(userAppRequestDTO.getPassword() != null && !userAppRequestDTO.getPassword().isEmpty()) {
            userAppRequestDTO.setPassword(passwordEncoder.encode(userAppRequestDTO.getPassword()));
        }
        Set<Role> roleSet = new HashSet<>();
        for(Role role : userAppRequestDTO.getRoleSet()) {
            roleService.findByIdOptional(role.getId()).ifPresent(roleSet::add);
        }
        userAppRequestDTO.setRoleSet(roleSet);
        UserApp userApp = userAppMapper.toUserApp(userAppRequestDTO);

        return userAppMapper.toUserAppResponseDTO(userAppRepository.save(userApp));
    }

    @Override
    public void deleteById(Long id) {
       if(!userAppRepository.existsById(id)) {
           throw new ResourceNotFoundException("User '" + id + "' not found.");
       }
        userAppRepository.deleteById(id);
    }

    @Override
    public UserAppResponseDTO updateById(Long id, UserAppRequestDTO userAppRequestDTO) {
        UserApp userAppFound = userAppRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User '"+ id +"' not found."));

        if(userAppRequestDTO.getPassword() != null && !userAppRequestDTO.getPassword().isEmpty()) {
            userAppFound.setPassword(passwordEncoder.encode(userAppRequestDTO.getPassword()));
        }
        Set<Role> roleSet = new HashSet<>();
        for(Role role : userAppRequestDTO.getRoleSet()) {
            roleService.findByIdOptional(role.getId()).ifPresent(roleSet::add);
        }
        userAppMapper.updateUserAppFromDTO(userAppRequestDTO,userAppFound);
        return userAppMapper.toUserAppResponseDTO(userAppRepository.save(userAppFound));
    }
}
