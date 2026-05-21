package com.e_commerce.repository;

import com.e_commerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ICartItemRepository extends JpaRepository<CartItem,Long> {

    public Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}
