package com.joshi.tmsapplication.controller;

import com.joshi.tmsapplication.entity.Order;
import com.joshi.tmsapplication.graphql.OrderPage;
import com.joshi.tmsapplication.graphql.input.CreateOrderInput;
import com.joshi.tmsapplication.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OrderGraphQLResolver {

    private final OrderService orderService;

    // ADMIN + EMPLOYEE can view orders
    // @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    // @QueryMapping
    // public OrderPage orders(@Argument int page, @Argument int size) {
    //     return orderService.getOrdersPage(page, size);
    // }

    // EMPLOYEE + ADMIN can create order
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @MutationMapping
    public Order createOrder(@Argument CreateOrderInput input) {
        return orderService.createOrderFromGraphQL(input);
    }

    // Only ADMIN can assign courier
    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Order assignCourier(
            @Argument Long orderId,
            @Argument Long courierId
    ) {
        return orderService.assignCourier(orderId, courierId);
    }
}
