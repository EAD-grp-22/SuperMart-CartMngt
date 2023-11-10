package com.supermart.order.service;

import com.supermart.order.dto.*;
import com.supermart.order.model.Cart;
import com.supermart.order.model.CartItem;
import com.supermart.order.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final OrderService orderService;
    private final CartMapperService cartMapperService;
    private final CallUserAPIService callUserAPIService;

    public CartResponse getCartByCustomerId(Integer customerId) {
        Cart cart = cartRepository.findCartByCustomerId(customerId);
        return cartMapperService.mapToCartResponse(cart);

    }

    private Integer assignCart(Integer customerId) {
        CartResponse existingCart = getCartByCustomerId(customerId);
        if (existingCart != null) {
            return existingCart.getId();
        }
        if(callUserAPIService.isCustomerValid(customerId)){
            Cart newCart = new Cart();
            newCart.setCustomerId(customerId);
            cartRepository.save(newCart);
            return newCart.getId();
        }
         throw new RuntimeException("Inavlid Customer");
    }

    public String addItemsToCart(CartRequest cartRequest) {
        Integer cartId=assignCart(cartRequest.getCustomerId());
        Cart cart=cartRepository.findCartById(cartId);

        for (CartItemRequest itemRequest : cartRequest.getCartItems()) {
            String skuCode = itemRequest.getSkuCode();
            Integer quantity = itemRequest.getQuantity();


            Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                    .filter(item -> item.getSkuCode().equals(skuCode))
                    .findFirst();

            if (existingCartItem.isPresent()) {
                CartItem cartItem = existingCartItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setSkuCode(skuCode);
                cartItem.setQuantity(quantity);
                cartItem.setCart(cart);
                cart.getCartItems().add(cartItem);
            }
        }
        cartRepository.save(cart);
        return "Items added to cart successfully";
    }


    public String removeItemsFromCart(CartRequest cartRequest) {
        Cart cart = cartRepository.findByCustomerId(cartRequest.getCustomerId());

        for (CartItemRequest itemRequest : cartRequest.getCartItems()) {
            String skuCode = itemRequest.getSkuCode();
            Integer quantity = itemRequest.getQuantity();

            Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                    .filter(item -> item.getSkuCode().equals(skuCode))
                    .findFirst();

            if (existingCartItem.isPresent()) {
                CartItem cartItem = existingCartItem.get();
                int newQuantity = cartItem.getQuantity() - quantity;
                if (newQuantity > 0) {
                    cartItem.setQuantity(newQuantity);
                } else {
                    cart.getCartItems().remove(cartItem);
                }
            }
        }
        cartRepository.save(cart);
        return "Items removed from cart successfully";
    }

    public String clearCart(Integer customerId) {
        Integer cartId = assignCart(customerId);
        Cart cart = cartRepository.findCartById(cartId);
        cart.getCartItems().clear();
        cartRepository.save(cart);
        return "Cart cleared successfully";
    }

    public String checkOutCart(Integer customerId,String shippingAddress){
        Integer cartId = assignCart(customerId);
        Cart cart = cartRepository.findCartById(cartId);

        if (cart.getCartItems().isEmpty()) {
            return "Cart is empty. Nothing to checkout.";
        }

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(customerId);
        orderRequest.setShippingAddress(shippingAddress);
        List<OrderItemRequest> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItemRequest orderItem=cartMapperService.mapToOrderItemRequest(cartItem);
            orderItems.add(orderItem);
        }
        orderRequest.setOrderItems(orderItems);
        String orderResult = orderService.createOrder(orderRequest);

        clearCart(customerId);
        return "Cart checked out successfully.\n" + orderResult;
    }


}
