package com.joshi.tmsapplication.service;

import com.joshi.tmsapplication.repository.OrderRepository;
import com.joshi.tmsapplication.repository.ProductRepository;
import com.joshi.tmsapplication.repository.UserRepository;
import com.joshi.tmsapplication.repository.AddressRepository;
import com.joshi.tmsapplication.dto.OrderDto;
import com.joshi.tmsapplication.dto.OrderItemDto;
import com.joshi.tmsapplication.entity.User;
import com.joshi.tmsapplication.entity.Addresses;
import com.joshi.tmsapplication.entity.Order;
import com.joshi.tmsapplication.entity.OrderItems;
import com.joshi.tmsapplication.entity.Product;
import com.joshi.tmsapplication.mapper.OrderMapper;
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

    public OrderDto createOrder(OrderDto dto, Long userId) {
        
        // Validate required fields
        if (dto.getPickupAddressId() == null) {
            throw new IllegalArgumentException("Pickup address ID must not be null");
        }
        if (dto.getDeliveryAddressId() == null) {
            throw new IllegalArgumentException("Delivery address ID must not be null");
        }
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Addresses pickup =
                addressRepository.findById(dto.getPickupAddressId())
                        .orElseThrow(() -> new RuntimeException("Pickup address not found"));

        Addresses delivery =
                addressRepository.findById(dto.getDeliveryAddressId())
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

        Order savedOrder = orderRepository.save(order);

        return OrderMapper.toDto(savedOrder);
    }

    public OrderDto assignCourier(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus("ASSIGNED");
        return OrderMapper.toDto(orderRepository.save(order));
    }
}
