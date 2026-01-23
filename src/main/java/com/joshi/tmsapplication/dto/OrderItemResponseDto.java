package com.joshi.tmsapplication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}
