package com.example.controller;

import com.example.dto.AuthDTO;
import com.example.dto.ProfileDTO;
import com.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("reg")
    public ResponseEntity<?> registration(@RequestBody ProfileDTO dto) {
        return ResponseEntity.ok(authService.registration(dto));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        return ResponseEntity.ok(authService.login(authDTO));
    }

    public void ver() {
    }

    public void resent() {
    }
}
