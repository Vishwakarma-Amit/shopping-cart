package com.dreamshops.repository;

import com.dreamshops.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    void deleteAllByCartCartId(int cartId);

}
