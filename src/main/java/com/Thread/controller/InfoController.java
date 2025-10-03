package com.Thread.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@OpenAPIDefinition
@RestController
public class InfoController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/port")
    public String getPort() {
        return "Application is running on port: " + serverPort;
    }
}