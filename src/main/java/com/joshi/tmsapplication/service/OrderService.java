package com.joshi.tmsapplication.service;

import com.joshi.tmsapplication.dto.OrderDto;
import com.joshi.tmsapplication.dto.OrderItemDto;
import com.joshi.tmsapplication.entity.*;
import com.joshi.tmsapplication.mapper.OrderMapper;
import com.joshi.tmsapplication.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    /* =====================================================
       REST API: Create Order
       ===================================================== */
       @Transactional
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
        order.setStatus(OrderStatus.CREATED); // ✅ ENUM

        BigDecimal totalAmount = BigDecimal.ZERO;

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

            totalAmount = totalAmount.add(
                    product.getPrice()
                            .multiply(BigDecimal.valueOf(itemDto.getQuantity()))
            );
        }

        order.setAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toDto(savedOrder);
    }

    /* =====================================================
       REST API: Assign Courier
       ===================================================== */
    public OrderDto assignCourier(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.ASSIGNED); // ✅ ENUM
        return OrderMapper.toDto(orderRepository.save(order));
    }
}
