
package com.joshi.tmsapplication.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="pickup_address_id")
    private Addresses pickupAddress;

    @ManyToOne
    @JoinColumn(name="delivery_address_id")
    private Addresses deliveryAddress;
    

    private String status; // CREATED, ASSIGNED, IN_TRANSIT, DELIVERED, CANCELLED

    private BigDecimal amount;

    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItems> items = new ArrayList<>();

}
