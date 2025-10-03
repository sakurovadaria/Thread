package com.Thread.controller;

import com.Thread.service.MathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/math")
public class MathController {
    private final MathService mathService;

    public MathController(MathService mathService) {
        this.mathService = mathService;
    }

    @GetMapping("/sum")
    public ResponseEntity<Long> getSum() {
        long sum = mathService.calculateSum();
        return ResponseEntity.ok(sum);
    }

}
