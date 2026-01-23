package com.joshi.tmsapplication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderItemDto {
    private Long productId;
    private Integer quantity;
}