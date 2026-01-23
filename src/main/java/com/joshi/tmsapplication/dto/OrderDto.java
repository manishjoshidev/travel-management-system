package com.joshi.tmsapplication.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class OrderDto {

    private Long id;
    private Long userId;

    private Long pickupAddressId;
    private Long deliveryAddressId;

    private String status;
    private BigDecimal amount;
    private Instant createdAt;

    private List<OrderItemDto> items;
}
