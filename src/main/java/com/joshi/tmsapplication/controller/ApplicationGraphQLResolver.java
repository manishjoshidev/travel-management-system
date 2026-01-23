package com.joshi.tmsapplication.controller;

import com.joshi.tmsapplication.dto.OrderDto;
import com.joshi.tmsapplication.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.Argument;

@Controller
@RequiredArgsConstructor
public class ApplicationGraphQLResolver {

    private final OrderService orderService;

    @MutationMapping
    public OrderDto createOrder(
            @Argument OrderDto order,
            @Argument Long userId
    ) {
        return orderService.createOrder(order, userId);
    }

    @MutationMapping
    public OrderDto assignCourier(@Argument Long orderId) {
        return orderService.assignCourier(orderId);
    }
}
