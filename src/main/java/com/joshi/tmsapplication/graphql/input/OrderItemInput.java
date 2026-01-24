package com.joshi.tmsapplication.graphql.input;

import lombok.Data;

@Data
public class OrderItemInput {

    private Long productId;
    private int quantity;
}
