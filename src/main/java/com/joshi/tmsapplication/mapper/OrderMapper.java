

package com.joshi.tmsapplication.mapper;

import java.util.stream.Collectors;
import com.joshi.tmsapplication.dto.OrderDto;
import com.joshi.tmsapplication.dto.OrderItemDto;
import com.joshi.tmsapplication.entity.Order;
import com.joshi.tmsapplication.entity.OrderItems;

public class OrderMapper {

    public static OrderDto toDto(Order order) {

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setPickupAddressId(order.getPickupAddress().getId());
        dto.setDeliveryAddressId(order.getDeliveryAddress().getId());
        dto.setStatus(order.getStatus());
        dto.setAmount(order.getAmount());
        dto.setCreatedAt(order.getCreatedAt());

        dto.setItems(
                order.getItems()
                        .stream()
                        .map(OrderMapper::toItemDto)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    private static OrderItemDto toItemDto(OrderItems item) {

        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getProductId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());

        return dto;
    }
}
