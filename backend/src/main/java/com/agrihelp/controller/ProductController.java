package com.agrihelp.controller;

import com.agrihelp.common.Result;
import com.agrihelp.dto.ProductDTO;
import com.agrihelp.service.ProductService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Result<Page<ProductDTO>> getProducts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String category) {
        return Result.success(productService.getProductPage(page, size, category));
    }

    @GetMapping("/{id}")
    public Result<ProductDTO> getProduct(@PathVariable Long id) {
        return Result.success(productService.getProductById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public Result<Void> createProduct(@RequestBody ProductDTO productDTO) {
        productService.createProduct(productDTO);
        return Result.success();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public Result<Void> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        productService.updateProduct(id, productDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success();
    }
}
