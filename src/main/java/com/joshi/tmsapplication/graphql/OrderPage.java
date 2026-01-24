package com.joshi.tmsapplication.graphql;

import com.joshi.tmsapplication.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderPage {

    private List<Order> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
