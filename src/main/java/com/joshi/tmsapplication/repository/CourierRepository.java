
package com.joshi.tmsapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.joshi.tmsapplication.entity.Courier;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
    List<Courier> findByStatus(String status);
}
