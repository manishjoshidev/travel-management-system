
package com.joshi.tmsapplication.controller;

import com.joshi.tmsapplication.entity.Courier;
import com.joshi.tmsapplication.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Courier> createCourier(@RequestParam Long userId) {
        return ResponseEntity.ok(courierService.createCourier(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Courier> getCourierById(@PathVariable Long id) {
        return courierService.getCourierById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Courier>> getAllCouriers() {
        return ResponseEntity.ok(courierService.getAllCouriers());
    }

    @GetMapping("/available")
    public ResponseEntity<List<Courier>> getAvailableCouriers() {
        return ResponseEntity.ok(courierService.getAvailableCouriers());
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DELIVERY_GUY')")
    public ResponseEntity<Courier> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(courierService.updateStatus(id, status));
    }
}
