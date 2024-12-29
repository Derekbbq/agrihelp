package com.agrihelp.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String orderNo;
    private BigDecimal totalAmount;
    private Integer status;
    private String address;
    private String phone;
    private String receiverName;
    private LocalDateTime createTime;
    private List<OrderItemDTO> items;
}
