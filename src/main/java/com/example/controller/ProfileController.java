package com.example.controller;



import com.example.dto.ProfileDTO;
import com.example.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProfileDTO dto) {
        return ResponseEntity.ok(profileService.create(dto));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody ProfileDTO dto) {
        profileService.update(dto);
        return ResponseEntity.ok("Update");
    }

    @PutMapping("/update/detail")
    public ResponseEntity<?> updateProfileDetail() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete() {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/photo")
    public ResponseEntity<?> updatePhoto() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter() {
        return ResponseEntity.ok().build();
    }
}
