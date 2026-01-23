package com.joshi.tmsapplication.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}
