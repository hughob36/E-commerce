package com.e_commerce.service;


import com.e_commerce.model.UserApp;

import java.util.List;
import java.util.Optional;

public interface IUserAppService {

    public List<UserApp> findAll();
    public UserApp findById(Long id);
    public UserApp save(UserApp userApp);
    public boolean deleteById(Long id);
    public UserApp updateById(Long id, UserApp userApp);
    public String encriptPassword(String password);
}
