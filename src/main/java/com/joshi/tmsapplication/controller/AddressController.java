
package com.joshi.tmsapplication.controller;

import com.joshi.tmsapplication.entity.Addresses;
import com.joshi.tmsapplication.entity.User;
import com.joshi.tmsapplication.repository.AddressRepository;
import com.joshi.tmsapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Addresses> createAddress(@RequestBody Addresses address, @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        address.setUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressRepository.save(address));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Addresses>> getUserAddresses(@PathVariable Long userId) {
        return ResponseEntity.ok(addressRepository.findByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Addresses> getAddress(@PathVariable Long id) {
        return ResponseEntity.ok(
            addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
