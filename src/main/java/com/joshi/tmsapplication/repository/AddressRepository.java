package com.joshi.tmsapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.joshi.tmsapplication.entity.Addresses;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Addresses, Long> {
    List<Addresses> findByUserId(Long userId);
}
