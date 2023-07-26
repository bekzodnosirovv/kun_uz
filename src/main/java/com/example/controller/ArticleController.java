package com.example.controller;

import com.example.dto.ArticleDTO;
import com.example.dto.JwtDTO;
import com.example.enums.ArticleStatus;
import com.example.enums.Language;
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

    @PostMapping(value = "/closed")
    public ResponseEntity<?> create(@RequestBody ArticleDTO dto,
                                    HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.create(jwtDTO.getId(), dto));
    }

    @PutMapping(value = "/closed/{id}")
    public ResponseEntity<?> update(@RequestBody ArticleDTO dto,
                                    @PathVariable("id") String id,
                                    HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.MODERATOR);
        articleService.update(id, dto, jwtDTO.getId());
        return ResponseEntity.ok("Update article !!!");
    }

    @DeleteMapping(value = "/closed/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id,
                                    HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.MODERATOR);
        articleService.delete(id);
        return ResponseEntity.ok("Article deleted !!!");
    }

    @PutMapping(value = "/closed/publish/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable("id") String id,
                                          @RequestParam("status") ArticleStatus status,
                                          HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.PUBLISHER);
        articleService.changeStatus(id, jwtDTO.getId(), status);
        return ResponseEntity.ok("Update article status");
    }

    @GetMapping(value = "/lastFive")
    public ResponseEntity<?> getLastFiveByType(@RequestParam("type") Integer id) {
        return ResponseEntity.ok(articleService.getLastFiveByType(id));
    }

    @GetMapping(value = "/lastThree")
    public ResponseEntity<?> getLastThreeByType(@RequestParam("type") Integer id) {
        return ResponseEntity.ok(articleService.getLastThreeByType(id));
    }

    @GetMapping(value = "/lastEight")
    public ResponseEntity<?> getLastEight(@RequestBody List<String> list) {
        return ResponseEntity.ok(articleService.getLastEight(list));
    }

    @GetMapping(value = "/{id}/lan")
    public ResponseEntity<?> getByIdAndLang(@PathVariable("id") String articleId,
                                            @RequestParam("lan") Language lan) {
        return ResponseEntity.ok(articleService.getByIdAndLan(articleId, lan));
    }

    @GetMapping(value = "/lastFour/{id}/type")
    public ResponseEntity<?> getLastFourByType(@PathVariable("id")String articleId,
                                               @RequestParam("type")Integer typeId) {
        return ResponseEntity.ok("");
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

    @GetMapping(value = "/lastFive/{id}")
    public ResponseEntity<?> getLastFiveByCategory(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(articleService.getLastFiveByCategory(id));
    }

    @GetMapping(value = "/byCategory/{catId}/page")
    public ResponseEntity<?> getByCategoryPagination(@PathVariable("catId") Integer catId,
                                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(articleService.byCategory(catId, page - 1, size));
    }

    @GetMapping(value = "/view/{id}")
    public ResponseEntity<?> increaseViewCountById(@PathVariable("id") String id) {
        return ResponseEntity.ok(articleService.increaseViewCountById(id));
    }

    @GetMapping(value = "/shared/{id}")
    public ResponseEntity<?> increaseShareCountById(@PathVariable("id") String id) {
        return ResponseEntity.ok(articleService.increaseShareCountById(id));
    }

    @GetMapping(value = "/filter")
    public ResponseEntity<?> filter() {
        return null;
    }

}
