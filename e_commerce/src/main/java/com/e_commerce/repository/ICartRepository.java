package com.e_commerce.repository;

import com.e_commerce.model.Cart;
import com.e_commerce.model.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<Cart,Long> {

    public Optional<Cart> findByUser(UserApp userApp);
}
