package com.joshi.tmsapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.joshi.tmsapplication.entity.Addresses;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Addresses, Long> {

    List<Addresses> findByUserId(Long userId);

    // Optional (GraphQL grid support)
    Page<Addresses> findByUserId(Long userId, Pageable pageable);
}
