package com.joshi.tmsapplication.controller;


import com.joshi.tmsapplication.dto.*;
import com.joshi.tmsapplication.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;




@RestController
@RequestMapping("/api/orders")

@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @RequestBody OrderDto dto,
            @RequestParam Long userId
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(dto, userId));
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<OrderDto> assign(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.assignCourier(id));
    }
}
