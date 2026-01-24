package com.joshi.tmsapplication.service;

import com.joshi.tmsapplication.entity.*;
import com.joshi.tmsapplication.graphql.input.CreateOrderInput;
import com.joshi.tmsapplication.graphql.input.OrderItemInput;

import com.joshi.tmsapplication.mapper.OrderMapper; 
import  com.joshi.tmsapplication.dto.*;
import com.joshi.tmsapplication.repository.*;


import lombok.RequiredArgsConstructor;


import java.math.BigDecimal;
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    /* =====================================================
       REST: Create Order (USED BY OrderController)
       ===================================================== */
    public OrderDto createOrder(OrderDto dto, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Addresses pickup = addressRepository.findById(dto.getPickupAddressId())
                .orElseThrow(() -> new RuntimeException("Pickup address not found"));

        Addresses delivery = addressRepository.findById(dto.getDeliveryAddressId())
                .orElseThrow(() -> new RuntimeException("Delivery address not found"));

        Order order = new Order();
        order.setUser(user);
        order.setPickupAddress(pickup);
        order.setDeliveryAddress(delivery);
        order.setStatus("CREATED");

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemDto itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItems item = new OrderItems();
            item.setProduct(product);
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(product.getPrice());
            item.setDescription(product.getDescription());
            item.setOrder(order);

            order.getItems().add(item);

            total = total.add(
                    product.getPrice()
                           .multiply(BigDecimal.valueOf(itemDto.getQuantity()))
            );
        }

        order.setAmount(total);
        return OrderMapper.toDto(orderRepository.save(order));
    }

    /* =====================================================
       REST: Assign Courier (USED BY OrderController)
       ===================================================== */
    public OrderDto assignCourier(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus("ASSIGNED");
        return OrderMapper.toDto(orderRepository.save(order));
    }

    /* =====================================================
       GraphQL: Create Order
       ===================================================== */
    public Order createOrderFromGraphQL(CreateOrderInput input) {

        User user = userRepository.findById(1L) // POC
                .orElseThrow(() -> new RuntimeException("User not found"));

        Addresses pickup = addressRepository.findById(input.getPickupAddressId())
                .orElseThrow(() -> new RuntimeException("Pickup address not found"));

        Addresses delivery = addressRepository.findById(input.getDeliveryAddressId())
                .orElseThrow(() -> new RuntimeException("Delivery address not found"));

        Order order = new Order();
        order.setUser(user);
        order.setPickupAddress(pickup);
        order.setDeliveryAddress(delivery);
        order.setStatus("CREATED");

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemInput item : input.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItems oi = new OrderItems();
            oi.setProduct(product);
            oi.setQuantity(item.getQuantity());
            oi.setPrice(product.getPrice());
            oi.setOrder(order);

            order.getItems().add(oi);

            total = total.add(
                    product.getPrice()
                           .multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }

        order.setAmount(total);
        return orderRepository.save(order);
    }

    /* =====================================================
       GraphQL: Assign Courier
       ===================================================== */
    public Order assignCourier(Long orderId, Long courierId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus("ASSIGNED");
        return orderRepository.save(order);
    }
}
