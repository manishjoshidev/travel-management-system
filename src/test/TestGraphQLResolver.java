package com.joshi.tmsapplication.graphql;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TestGraphQLResolver {
@QueryMapping
    public String ping() {
        return "pong";
}
}
