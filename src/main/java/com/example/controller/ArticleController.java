package com.example.controller;

import com.example.dto.ArticleDTO;
import com.example.dto.JwtDTO;
import com.example.enums.ArticleStatus;
import com.example.enums.ProfileRole;
import com.example.service.ArticleService;
import com.example.util.SecurityUtil;
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

    @PostMapping("/closed")
    public ResponseEntity<?> create(@RequestBody ArticleDTO dto,
                                    HttpServletRequest request) {
//        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.create(dto));
    }

    @PutMapping(value = "/closed/{id}")
    public ResponseEntity<?> update(@RequestBody ArticleDTO dto,
                                    @PathVariable("id") String id,
                                    HttpServletRequest request) {
        articleService.update();
        return ResponseEntity.ok("Update article !!!");
    }

    @DeleteMapping("/closed/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id,
                                    HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.MODERATOR);
        articleService.delete(id);
        return ResponseEntity.ok("Article deleted !!!");
    }

    @PutMapping("/closed/changeStatus/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable("id") String id,
                                          @RequestParam("status") ArticleStatus status,
                                          HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.PUBLISHER);
        articleService.changeStatus(id, jwtDTO.getId(), status);
        return ResponseEntity.ok("Article published !!!");
    }

    @GetMapping("/lastFive")
    public ResponseEntity<?> getLastFiveByType(@RequestParam("type") Integer id) {
        articleService.getLastFiveByType(id);
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
