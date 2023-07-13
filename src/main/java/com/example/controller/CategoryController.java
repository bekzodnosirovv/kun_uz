package com.example.controller;

import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<?> create() {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> update() {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getByLang() {
        return ResponseEntity.ok().build();
    }
}
