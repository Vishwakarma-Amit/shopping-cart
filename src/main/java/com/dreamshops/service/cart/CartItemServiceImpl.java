package com.dreamshops.service.cart;

import com.dreamshops.entity.Cart;
import com.dreamshops.entity.CartItem;
import com.dreamshops.entity.Product;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.CartItemRepository;
import com.dreamshops.repository.CartRepository;
import com.dreamshops.repository.ProductRepository;
import com.dreamshops.utility.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements  CartItemService{

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private final CartRepository cartRepository;

    private final CartService cartService;

    @Override
    @Transactional
    public void createCartItem(int cartId, int productId, int quantity) {

        final String methodName = "createCartItem";

        Cart cart = cartService.getCart(cartId);
        log.info("{} - cart found, id - {}", methodName, cart.getCartId());

        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException(Message.PRODUCT_NOT_FOUND+productId));
        log.info("{} - product found, productId - {}", methodName, product.getProductId());

        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getProductId()==productId)
                .findFirst().orElse(new CartItem());
        if (cartItem.getCartItemId() == 0) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
            log.info("Cart item created");
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            log.info("updated quantity of cart item");
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        log.info("{} - cartItem added to cart", methodName);

        cartRepository.save(cart);
        log.info("{} - cart saved ", methodName);
        cartItemRepository.save(cartItem);
        log.info("{} - cartItem saved ", methodName);
    }

    @Override
    public void removeItemFromCart(int cartId, int productId) {
        final String methodName = "removeItemFromCart";
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item-> item.getProduct().getProductId()==productId)
                .findFirst().orElseThrow(()->new ResourceNotFoundException(Message.PRODUCT_NOT_FOUND+productId));
        log.info("{} - cartItem found from cart", methodName);

        cart.removeItem(cartItem);
        log.info("{} - cartItem removed from cart", methodName);

        cartRepository.save(cart);
        log.info("{} - cart details saved!", methodName);
    }

    @Override
    public void updateCartItemQuantity(int cartId, int productId, int quantity) {
        final String methodName = "updateCartItemQuantity";
        Cart cart = cartService.getCart(cartId);
        cart.getCartItems().stream()
                .filter(item-> item.getProduct().getProductId()==productId)
                .findFirst()
                .ifPresent(item->{
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        log.info("{} - cartItem found from cart by product id - {}", methodName, productId);
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
        log.info("{} - cart details updated!", methodName);
    }

    @Override
    public CartItem getCartItem(int cartId, int productId){
        final String methodName = "getCartItem";
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item-> item.getProduct().getProductId()==productId)
                .findFirst().orElseThrow(()->new ResourceNotFoundException(Message.PRODUCT_NOT_FOUND+productId));
        log.info("{} - cartItem retrieved from cart by product id - {}", methodName, productId);
        return cartItem;
    }
}
