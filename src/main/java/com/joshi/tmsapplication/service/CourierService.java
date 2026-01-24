

package com.joshi.tmsapplication.service;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.joshi.tmsapplication.entity.Courier;
import com.joshi.tmsapplication.entity.User;
import com.joshi.tmsapplication.repository.CourierRepository;
import com.joshi.tmsapplication.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierRepository courierRepository;
    private final UserRepository userRepository;

    public Courier createCourier(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Courier courier = new Courier();
        courier.setUser(user);
        courier.setStatus("AVAILABLE");
        return courierRepository.save(courier);
    }

    public Courier updateStatus(Long courierId, String status) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Courier not found"));
        courier.setStatus(status);
        return courierRepository.save(courier);
    }

    public Optional<Courier> getCourierById(Long id) {
        return courierRepository.findById(id);
    }

    public List<Courier> getAllCouriers() {
        return courierRepository.findAll();
    }

    public List<Courier> getAvailableCouriers() {
        return courierRepository.findByStatus("AVAILABLE");
    }
    public List<Courier> getCouriers(int page, int size) {
    Page<Courier> courierPage =
            courierRepository.findAll(PageRequest.of(page, size));
    return courierPage.getContent();
}
}
