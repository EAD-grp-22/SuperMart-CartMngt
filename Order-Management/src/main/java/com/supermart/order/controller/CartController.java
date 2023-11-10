package com.supermart.order.controller;

import com.supermart.order.dto.CartRequest;
import com.supermart.order.dto.CartResponse;
import com.supermart.order.dto.CheckOutCartRequest;
import com.supermart.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("api/order/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/customer/{customer-id}")
    public CartResponse getCartByCustomerId(@PathVariable("customer-id") Integer customerId){
        return cartService.getCartByCustomerId(customerId);
    }

    @PostMapping("/add-items")
    public String addItemsToCart(@RequestBody  CartRequest cartRequest){
        return cartService.addItemsToCart(cartRequest);
    }

    @PatchMapping("/remove-items")
    public String removeItemsFromCart(@RequestBody CartRequest cartRequest){
        return cartService.removeItemsFromCart(cartRequest);
    }

    @GetMapping("/clear/{customer-id}")
    public String clearCart(@PathVariable("customer-id") Integer customerId){
        return cartService.clearCart(customerId);
    }

    @PostMapping("/check-out")
    public String checkOutCart(@RequestBody CheckOutCartRequest request){
        return cartService.checkOutCart(request.getCustomerId(),request.getShippingAddress());
    }

}
