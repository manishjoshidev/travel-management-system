package com.joshi.tmsapplication.repository;

import com.joshi.tmsapplication.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Nothing extra needed for now
}
