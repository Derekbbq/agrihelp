package com.agrihelp.service;

import com.agrihelp.dto.CreateOrderRequest;
import com.agrihelp.dto.OrderDTO;
import com.agrihelp.dto.OrderItemDTO;
import com.agrihelp.entity.*;
import com.agrihelp.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public OrderDTO createOrder(CreateOrderRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        // 1. 获取购物车项
        List<CartItem> cartItems = cartItemMapper.selectBatchIds(request.getCartItemIds());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车项不存在");
        }

        // 2. 创建订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setOrderNo(generateOrderNo());
        order.setStatus(0);
        order.setAddress(request.getAddress());
        order.setPhone(request.getPhone());
        order.setReceiverName(request.getReceiverName());

        // 3. 创建订单项并计算总金额
        AtomicReference<BigDecimal> totalAmount = new AtomicReference<>(BigDecimal.ZERO);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            Product product = productMapper.selectById(cartItem.getProductId());
            if (product == null || product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("商品库存不足");
            }

            // 扣减库存
            product.setStock(product.getStock() - cartItem.getQuantity());
            productMapper.updateById(product);

            // 创建订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getImage());

            totalAmount.set(totalAmount.get().add(product.getPrice().multiply(new BigDecimal(cartItem.getQuantity()))));
            return orderItem;
        }).collect(Collectors.toList());

        order.setTotalAmount(totalAmount.get());
        orderMapper.insert(order);

        // 4. 保存订单项
        orderItems.forEach(item -> {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        });

        // 5. 清除购物车
        cartItemMapper.deleteBatchIds(request.getCartItemIds());

        return getOrderById(order.getId());
    }

    public OrderDTO getOrderById(Long orderId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(user.getId())) {
            throw new RuntimeException("订单不存在");
        }

        return convertToOrderDTO(order);
    }

    public Page<OrderDTO> getUserOrders(Integer page, Integer size) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        Page<Order> orderPage = new Page<>(page, size);
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, user.getId())
                   .orderByDesc(Order::getCreateTime);

        Page<Order> result = orderMapper.selectPage(orderPage, queryWrapper);

        List<OrderDTO> orderDTOs = result.getRecords().stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());

        Page<OrderDTO> dtoPage = new Page<>();
        BeanUtils.copyProperties(result, dtoPage, "records");
        dtoPage.setRecords(orderDTOs);

        return dtoPage;
    }

    @Transactional
    public void updateOrderStatus(Long orderId, Integer status) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(user.getId())) {
            throw new RuntimeException("订单不存在");
        }

        order.setStatus(status);
        orderMapper.updateById(order);
    }

    private OrderDTO convertToOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        BeanUtils.copyProperties(order, dto);

        // 获取订单项
        LambdaQueryWrapper<OrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderItem::getOrderId, order.getId());
        List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);

        List<OrderItemDTO> itemDTOs = orderItems.stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            BeanUtils.copyProperties(item, itemDTO);
            return itemDTO;
        }).collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }

    private String generateOrderNo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%04d", (int) (Math.random() * 10000));
    }
}
