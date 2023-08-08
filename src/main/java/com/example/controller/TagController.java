package com.example.controller;

import com.example.dto.TagDTO;
import com.example.service.TagService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody TagDTO dto,
                                    HttpServletRequest request) {
        return ResponseEntity.ok(tagService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer tagId,
                                    HttpServletRequest request) {
      return ResponseEntity.ok(tagService.change(tagId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer tagId,
                                    HttpServletRequest request) {
        return ResponseEntity.ok(tagService.delete(tagId));
    }

    @GetMapping("")
    public ResponseEntity<?> get(HttpServletRequest request) {
        return ResponseEntity.ok(tagService.get());
    }

}
