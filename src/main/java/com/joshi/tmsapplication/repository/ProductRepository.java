package com.joshi.tmsapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import com.joshi.tmsapplication.entity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String productName);
    Optional<Product> findBySku(String sku);
}
