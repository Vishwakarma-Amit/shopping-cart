package com.dreamshops.service.cart;

import com.dreamshops.dto.CartDto;
import com.dreamshops.entity.Cart;
import com.dreamshops.entity.User;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.CartItemRepository;
import com.dreamshops.repository.CartRepository;
import com.dreamshops.utility.Converter;
import com.dreamshops.utility.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final Converter converter;

    @Override
    public Cart initializeCart(User user){
        final String methodName = "initializeCart";
        Cart cart = new Cart();

        cart.setUser(user);
        log.info("{} - cart created", methodName);

        Cart savedCart = cartRepository.save(cart);
        log.info("{} - cart initialized, cart id - {}", methodName, savedCart.getCartId());
        return savedCart;
    }

    @Override
    public Cart getCart(int cartId) {
        final String methodName = "getCart";

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()->new ResourceNotFoundException(Message.CART_NOT_FOUND +cartId));
        log.info("{} - cart retrieved by id - {}", methodName, cartId);
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
        log.info("{} - cart saved successfully!", methodName);
        return cart;
    }

    @Transactional
    @Override
    public void clearCart(int cartId) {
        final String methodName = "clearCart";
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(Message.CART_NOT_FOUND + cartId));
        log.info("{} - cart found with id - {}", methodName, cartId);

        // Clear cart items
        cartItemRepository.deleteAllByCartCartId(cartId);
        cart.getCartItems().clear();
        log.info("{} - removed all the cart items from cart with id - {}", methodName, cartId);

        // Nullify the user reference in the cart
        if (cart.getUser() != null) {
            User user = cart.getUser();
            user.setCart(null); // Remove reference to cart from user
            cart.setUser(null); // Remove reference to user from cart
        }

        // Delete the cart
        cartRepository.delete(cart);
        log.info("{} - removed cart with id - {}", methodName, cartId);
    }


    @Override
    public BigDecimal getTotalPrice(int cartId) {
        final String methodName = "getTotalPrice";
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()->new ResourceNotFoundException(Message.CART_NOT_FOUND+cartId));
        log.info("{} - retrieved cart with id - {}", methodName, cartId);
        return cart.getTotalAmount();
    }

    @Override
    public CartDto getCartById(int cartId) {
        final String methodName = "getCartById";

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()->new ResourceNotFoundException(Message.CART_NOT_FOUND +cartId));
        log.info("{} - cart found by cart id - {}", methodName, cartId);

        return converter.convertToDto(cart);
    }

    @Override
    public Cart getCartByUserId(int userId) {
        final String methodName = "getCartByUserId";
        log.info("{} - invoked with user id - {}",methodName, userId);
        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(()->new ResourceNotFoundException(Message.CART_NOT_FOUND_WITH_USERID+userId));
        log.info("{} - cart retrieved by user id - {}",methodName, userId);
        return cart;
    }

}
