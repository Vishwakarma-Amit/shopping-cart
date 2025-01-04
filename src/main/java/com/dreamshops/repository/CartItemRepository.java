package com.dreamshops.repository;

import com.dreamshops.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    void deleteAllByCartCartId(int cartId);

    List<CartItem> findByCartCartId(int cartId);
}
