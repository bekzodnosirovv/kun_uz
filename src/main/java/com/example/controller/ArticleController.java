package com.example.controller;

import com.example.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String authToken) {
        return null;
    }

    @PutMapping("")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String authToken) {
        return null;
    }

    @DeleteMapping("")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String authToken) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeStatus(@RequestHeader("Authorization") String authToken) {
        return null;
    }


}
