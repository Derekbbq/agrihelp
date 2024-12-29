package com.agrihelp.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private Integer quantity;
    private BigDecimal price;
}
