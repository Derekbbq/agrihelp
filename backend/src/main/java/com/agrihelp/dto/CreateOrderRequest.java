package com.agrihelp.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {
    private String address;
    private String phone;
    private String receiverName;
    private List<Long> cartItemIds;
}
