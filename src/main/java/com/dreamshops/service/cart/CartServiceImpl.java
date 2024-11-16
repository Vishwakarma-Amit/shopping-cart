package com.dreamshops.service.cart;

import com.dreamshops.entity.Cart;
import com.dreamshops.entity.CartItem;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.CartItemRepository;
import com.dreamshops.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public Cart getCart(int cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()->new ResourceNotFoundException("Cart not found with cart id: "+cartId));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(int cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()->new ResourceNotFoundException("Cart not found with cart id: "+cartId));
        cartItemRepository.deleteAllByCartCartId(cartId);
        cart.getCartItems().clear();
        cartRepository.deleteById(cartId);
    }

    @Override
    public BigDecimal getTotalPrice(int cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()->new ResourceNotFoundException("Cart not found with cart id: "+cartId));
        return cart.getTotalAmount();
    }
}
