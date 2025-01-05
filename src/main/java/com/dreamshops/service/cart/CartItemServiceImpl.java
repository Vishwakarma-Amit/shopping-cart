package com.dreamshops.service.cart;

import com.dreamshops.dto.CartItemDto;
import com.dreamshops.entity.Cart;
import com.dreamshops.entity.CartItem;
import com.dreamshops.entity.Product;
import com.dreamshops.entity.User;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.CartRepository;
import com.dreamshops.repository.ProductRepository;
import com.dreamshops.repository.UserRepository;
import com.dreamshops.request.CartItemRequest;
import com.dreamshops.utility.Message;
import com.dreamshops.utility.Converter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements  CartItemService{

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final Converter converter;

    @Override
    @Transactional
    public void createCartItem(CartItemRequest cartItemRequest) {
        final String methodName = "createCartItem";

        int productId = cartItemRequest.getProductId();
        int quantity = cartItemRequest.getQuantity();

        User user = userRepository.findById(cartItemRequest.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException(Message.USER_NOT_FOUND + cartItemRequest.getUserId()));

        Cart cart;

        if(user!=null && user.getCart()==null) {
            cart = cartService.getCart(cartService.initializeCart(cartItemRequest.getUserId()));
        }else{
            cart = Objects.requireNonNull(user).getCart();
        }
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
        cart.updateTotalAmount();
        cartRepository.save(cart);
        log.info("{} - cart details updated!", methodName);
    }

    @Override
    public CartItemDto getCartItem(int cartId, int productId){
        final String methodName = "getCartItem";
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item-> item.getProduct().getProductId()==productId)
                .findFirst().orElseThrow(()->new ResourceNotFoundException(Message.PRODUCT_NOT_FOUND+productId));
        log.info("{} - cartItem retrieved from cart by product id - {}", methodName, productId);
        return converter.convertToDto(cartItem);
    }

}
