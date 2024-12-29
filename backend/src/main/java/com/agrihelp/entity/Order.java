package com.agrihelp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("`order`")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    private String orderNo;
    private BigDecimal totalAmount;
    private Integer status; // 0:待付款 1:待发货 2:待收货 3:已完成 4:已取消
    private String address;
    private String phone;
    private String receiverName;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
