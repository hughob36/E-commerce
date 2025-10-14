package com.e_commerce.service;

import com.e_commerce.model.UserApp;
import com.e_commerce.repository.IUserAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAppService implements IUserAppService{

    @Autowired
    private IUserAppRepository userAppRepository;

    @Override
    public List<UserApp> findAll() {
        return userAppRepository.findAll()
                ;
    }

    @Override
    public Optional<UserApp> findById(Long id) {
        return userAppRepository.findById(id);
    }

    @Override
    public UserApp save(UserApp userApp) {
        return userAppRepository.save(userApp);
    }

    @Override
    public boolean deleteById(Long id) {

        UserApp userApp = userAppRepository.findById(id).orElse(null);

        if(userApp != null) {
            userAppRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public UserApp updateById(Long id, UserApp userApp) {

        UserApp userAppFound = userAppRepository.findById(id).orElse(null);
        if(userAppFound != null) {
            userAppFound.setName(userApp.getName());
            userAppFound.setLatsName(userApp.getLatsName());
            userAppFound.setUsername(userApp.getUsername());
            userAppFound.setPassword(userApp.getPassword());
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
        return userAppFound;
    }

    @Override
    public String encriptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
