package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AuthDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.RegDTO;
import com.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

      @PostMapping(value = {"/registration"})           //    new
    public ResponseEntity<ApiResponseDTO> registration(@RequestBody RegDTO dto) {
        return ResponseEntity.ok(authService.registration(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        return ResponseEntity.ok(authService.login(authDTO));
    }

    @GetMapping(value = {"/verification/email/{jwt}"})
    public ResponseEntity<ApiResponseDTO> verificationEmail(@PathVariable("jwt") String jwt) {
        return ResponseEntity.ok(authService.emailVerification(jwt));
    }

    public void ver() {
    }

    public void resent() {
    }
}
