package com.agrihelp.service;

import com.agrihelp.dto.CartItemDTO;
import com.agrihelp.entity.CartItem;
import com.agrihelp.entity.Product;
import com.agrihelp.entity.User;
import com.agrihelp.mapper.CartItemMapper;
import com.agrihelp.mapper.ProductMapper;
import com.agrihelp.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    public List<CartItemDTO> getCurrentUserCart() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        LambdaQueryWrapper<CartItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CartItem::getUserId, user.getId());
        List<CartItem> cartItems = cartItemMapper.selectList(queryWrapper);

        return cartItems.stream().map(item -> {
            CartItemDTO dto = new CartItemDTO();
            Product product = productMapper.selectById(item.getProductId());
            
            dto.setId(item.getId());
            dto.setProductId(product.getId());
            dto.setProductName(product.getName());
            dto.setProductImage(product.getImage());
            dto.setPrice(product.getPrice());
            dto.setQuantity(item.getQuantity());
            dto.setStock(product.getStock());
            
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void addToCart(Long productId, Integer quantity) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        if (product.getStock() < quantity) {
            throw new RuntimeException("商品库存不足");
        }

        LambdaQueryWrapper<CartItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CartItem::getUserId, user.getId())
                   .eq(CartItem::getProductId, productId);
        CartItem existingItem = cartItemMapper.selectOne(queryWrapper);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemMapper.updateById(existingItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(user.getId());
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItemMapper.insert(cartItem);
        }
    }

    @Transactional
    public void updateCartItemQuantity(Long cartItemId, Integer quantity) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        CartItem cartItem = cartItemMapper.selectById(cartItemId);
        if (cartItem == null || !cartItem.getUserId().equals(user.getId())) {
            throw new RuntimeException("购物车项不存在");
        }

        Product product = productMapper.selectById(cartItem.getProductId());
        if (product.getStock() < quantity) {
            throw new RuntimeException("商品库存不足");
        }

        cartItem.setQuantity(quantity);
        cartItemMapper.updateById(cartItem);
    }

    @Transactional
    public void removeFromCart(Long cartItemId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        CartItem cartItem = cartItemMapper.selectById(cartItemId);
        if (cartItem == null || !cartItem.getUserId().equals(user.getId())) {
            throw new RuntimeException("购物车项不存在");
        }

        cartItemMapper.deleteById(cartItemId);
    }

    @Transactional
    public void clearCart() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        LambdaQueryWrapper<CartItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CartItem::getUserId, user.getId());
        cartItemMapper.delete(queryWrapper);
    }
}
