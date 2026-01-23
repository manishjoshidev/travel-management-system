
package com.joshi.tmsapplication.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data

public class DeliveryAssignments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name="courier_id")
    private Courier courier;

    @CreationTimestamp
    private LocalDateTime assignedAt;

    private LocalDateTime acceptedAt;

    private LocalDateTime deliveredAt;



}
