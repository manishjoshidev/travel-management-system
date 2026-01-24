package com.joshi.tmsapplication.graphql.input;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderInput {

    private Long pickupAddressId;
    private Long deliveryAddressId;
    private List<OrderItemInput> items;
}
