package com.example.controller;

import com.example.dto.JwtDTO;
import com.example.service.ArticleLikeService;
import com.example.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/articleLike")
public class ArticleLikeController {
    @Autowired
    private ArticleLikeService articleLikeService;

    @PostMapping(value = "/like/{articleId}")
    public ResponseEntity<?> like(@PathVariable("articleId") String articleId,
                                  HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, null);
        return ResponseEntity.ok(articleLikeService.like(jwtDTO.getId(), articleId));
    }

    @PostMapping(value = "/dislike/{articleId}")
    public ResponseEntity<?> dislike(@PathVariable("articleId") String articleId,
                                     HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, null);
        return ResponseEntity.ok(articleLikeService.dislike(jwtDTO.getId(), articleId));
    }

    @DeleteMapping(value = "/remove/{articleId}")
    public ResponseEntity<?> remove(@PathVariable("articleId") String articleId,
                                    HttpServletRequest request) {
        JwtDTO jwtDTO=SecurityUtil.hasRole(request,null);
        return ResponseEntity.ok(articleLikeService.remove(jwtDTO.getId(),articleId));
    }
}
