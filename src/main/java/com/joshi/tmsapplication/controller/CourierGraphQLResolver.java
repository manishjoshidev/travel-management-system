package com.joshi.tmsapplication.controller;

import com.joshi.tmsapplication.entity.Courier;
import com.joshi.tmsapplication.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CourierGraphQLResolver {

    private final CourierService courierService;

    /* ================================
       Query: Couriers (Grid / Tile)
       ================================ */
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @QueryMapping
    public List<Courier> couriers(
            @Argument int page,
            @Argument int size
    ) {
        return courierService.getCouriers(page, size);
    }

    /* ================================
       Mutation: Update Courier Status
       ================================ */
    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Courier updateCourierStatus(
            @Argument Long courierId,
            @Argument String status
    ) {
        return courierService.updateStatus(courierId, status);
    }
}
