package com.example.demo.controller;


import com.example.demo.filter.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private RateLimiter rateLimiter;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        String key = "hello";
        if (rateLimiter.allowRequest(key)) {
            return ResponseEntity.ok("{\"msg\":\"hello\"}");
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("429 Too Many Requests");
        }
    }
}
