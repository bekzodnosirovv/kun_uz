package com.example.controller;

import com.example.dto.ArticleDTO;
import com.example.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/close")
    public ResponseEntity<?> create(@RequestBody ArticleDTO dto,
                                    HttpServletRequest request) {

        return null;
    }

    @PutMapping("/close")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String authToken) {
        return null;
    }

    @DeleteMapping("close")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String authToken) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeStatus(@RequestHeader("Authorization") String authToken) {
        return null;
    }

    @GetMapping("/5")
    public ResponseEntity<?> getLast5ByType(@RequestParam("type") Integer id) {
        return null;
    }

    @GetMapping("/3")
    public ResponseEntity<?> getLast53ByType(@RequestParam("type") Integer id) {
        return null;
    }

    @GetMapping("/8")
    public ResponseEntity<?> getLast8(@RequestBody List<String> list) {
        return null;
    }

    public ResponseEntity<?> getByIdAndLang() {
        return null;
    }

    public ResponseEntity<?> getLast4ByType() {
        return null;
    }

    public ResponseEntity<?> getMostRead4() {
        return null;
    }

    public ResponseEntity<?> getLast4ByTagName() {
        return null;
    }

    public ResponseEntity<?> getLast5ByTypeAndRegion() {
        return null;
    }

    public ResponseEntity<?> getByRegionPagination() {
        return null;
    }

    public ResponseEntity<?> getLast5ByCategory() {
        return null;
    }

    public ResponseEntity<?> getByCategoryPagination() {
        return null;
    }

    @PutMapping("/view/{id}")
    public ResponseEntity<?> increaseViewCountById(@PathVariable("id") String id) {
        return null;
    }

    @PutMapping("/shared/{id}")
    public ResponseEntity<?> increaseShareCountById(@PathVariable("id") String id) {
        return null;
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter() {
        return null;
    }

}
