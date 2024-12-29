package com.agrihelp.controller;

import com.agrihelp.common.Result;
import com.agrihelp.dto.CartItemDTO;
import com.agrihelp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public Result<List<CartItemDTO>> getCart() {
        return Result.success(cartService.getCurrentUserCart());
    }

    @PostMapping
    public Result<Void> addToCart(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        cartService.addToCart(productId, quantity);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> updateCartItemQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        cartService.updateCartItemQuantity(id, quantity);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return Result.success();
    }

    @DeleteMapping
    public Result<Void> clearCart() {
        cartService.clearCart();
        return Result.success();
    }
}
