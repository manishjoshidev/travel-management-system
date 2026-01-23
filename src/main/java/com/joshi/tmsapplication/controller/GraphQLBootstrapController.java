package com.joshi.tmsapplication.controller;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GraphQLBootstrapController {

    @QueryMapping
    public String ping() {
        return "pong";
    }
}
