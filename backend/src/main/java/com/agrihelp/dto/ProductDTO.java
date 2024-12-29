package com.agrihelp.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String image;
    private String category;
    private String sellerName;
    private Integer status;
}
