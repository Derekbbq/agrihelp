package com.agrihelp.controller;

import com.agrihelp.common.Result;
import com.agrihelp.dto.CreateOrderRequest;
import com.agrihelp.dto.OrderDTO;
import com.agrihelp.service.OrderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Result<OrderDTO> createOrder(@RequestBody CreateOrderRequest request) {
        return Result.success(orderService.createOrder(request));
    }

    @GetMapping("/{id}")
    public Result<OrderDTO> getOrder(@PathVariable Long id) {
        return Result.success(orderService.getOrderById(id));
    }

    @GetMapping
    public Result<Page<OrderDTO>> getUserOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(orderService.getUserOrders(page, size));
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        orderService.updateOrderStatus(id, status);
        return Result.success();
    }
}
