package com.joshi.tmsapplication.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import com.joshi.tmsapplication.entity.OrderStatus;

@Data
public class OrderDto {

    private Long id;
    private Long userId;
   @NotNull
    private Long pickupAddressId;
       @NotBlank
    private Long deliveryAddressId;

    
    private OrderStatus status;

    private BigDecimal amount;
    private Instant createdAt;

    private List<OrderItemDto> items;
}
