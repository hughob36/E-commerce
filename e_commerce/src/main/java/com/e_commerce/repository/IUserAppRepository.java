package com.e_commerce.repository;

import com.e_commerce.model.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserAppRepository extends JpaRepository<UserApp, Long> {

    public Optional<UserApp> findEntityUserAppByUsername(String username);
}
