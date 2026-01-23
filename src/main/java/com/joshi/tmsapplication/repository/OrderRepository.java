
package com.joshi.tmsapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joshi.tmsapplication.entity.Order;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser_Id(Long userId);

    List<Order> findByStatus(String status);
}
