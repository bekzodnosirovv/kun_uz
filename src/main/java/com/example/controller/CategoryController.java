package com.example.controller;

import com.example.dto.CategoryDTO;
import com.example.enums.Language;
import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String authToken,
                                    @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(categoryService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String authToken,
                                    @PathVariable("id") Integer id,
                                    @RequestBody CategoryDTO dto) {
        categoryService.update(id, dto);
        return ResponseEntity.ok("Category update !!!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String authToken,
                                    @PathVariable("id") Integer id) {
        categoryService.delete(id);
        return ResponseEntity.ok("Category deleted !!!");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String authToken) {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/lan")
    public ResponseEntity<?> getByLan(@RequestHeader("Authorization") String authToken,
                                      @RequestParam("lan") Language lan) {
        return ResponseEntity.ok(categoryService.getByLan(lan));
    }
}
