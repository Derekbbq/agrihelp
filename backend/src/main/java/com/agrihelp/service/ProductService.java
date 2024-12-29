package com.agrihelp.service;

import com.agrihelp.dto.ProductDTO;
import com.agrihelp.entity.Product;
import com.agrihelp.entity.User;
import com.agrihelp.mapper.ProductMapper;
import com.agrihelp.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    public Page<ProductDTO> getProductPage(Integer page, Integer size, String category) {
        Page<Product> productPage = new Page<>(page, size);
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        
        if (category != null && !category.isEmpty()) {
            queryWrapper.eq(Product::getCategory, category);
        }
        queryWrapper.eq(Product::getStatus, 0); // 只查询上架商品
        
        Page<Product> result = productMapper.selectPage(productPage, queryWrapper);
        
        return convertToProductDTOPage(result);
    }

    public ProductDTO getProductById(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        return convertToProductDTO(product);
    }

    @Transactional
    public void createProduct(ProductDTO productDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User seller = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setSellerId(seller.getId());
        product.setStatus(0); // 默认上架

        productMapper.insert(product);
    }

    @Transactional
    public void updateProduct(Long id, ProductDTO productDTO) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User seller = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        if (!product.getSellerId().equals(seller.getId())) {
            throw new RuntimeException("无权修改此商品");
        }

        BeanUtils.copyProperties(productDTO, product);
        product.setId(id);
        product.setSellerId(seller.getId());

        productMapper.updateById(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User seller = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        if (!product.getSellerId().equals(seller.getId())) {
            throw new RuntimeException("无权删除此商品");
        }

        productMapper.deleteById(id);
    }

    private ProductDTO convertToProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        BeanUtils.copyProperties(product, dto);
        
        // 获取卖家信息
        User seller = userMapper.selectById(product.getSellerId());
        if (seller != null) {
            dto.setSellerName(seller.getNickname());
        }
        
        return dto;
    }

    private Page<ProductDTO> convertToProductDTOPage(Page<Product> productPage) {
        List<ProductDTO> productDTOs = productPage.getRecords().stream()
                .map(this::convertToProductDTO)
                .collect(Collectors.toList());

        Page<ProductDTO> dtoPage = new Page<>();
        BeanUtils.copyProperties(productPage, dtoPage, "records");
        dtoPage.setRecords(productDTOs);

        return dtoPage;
    }
}
