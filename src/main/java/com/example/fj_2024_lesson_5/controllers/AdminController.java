package com.example.fj_2024_lesson_5.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @PostMapping("/some-endpoint")
    public ResponseEntity<String> someEndpoint() {
        return ResponseEntity.ok("Admin Access Granted");
    }
}