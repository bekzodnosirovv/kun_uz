package com.example.controller;

import com.example.dto.CommentLikeDTO;
import com.example.dto.JwtDTO;
import com.example.service.CommentLikeService;
import com.example.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/commentLike/")
public class CommentLikeController {

    @Autowired
    private CommentLikeService commentLikeService;

    @PostMapping(value = "/like")
    public ResponseEntity<?> like(@RequestBody CommentLikeDTO likeDTO,
                                  HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, null);
        return ResponseEntity.ok(commentLikeService.like(jwtDTO.getId(), likeDTO));
    }

    @PostMapping(value = "/dislike")
    public ResponseEntity<?> dislike(@RequestBody CommentLikeDTO likeDTO,
                                     HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, null);
        return ResponseEntity.ok(commentLikeService.dislike(jwtDTO.getId(), likeDTO));
    }

    @DeleteMapping(value = "/remove")
    public ResponseEntity<?> remove(@RequestParam("id") String commentId,
                                    HttpServletRequest request) {
        JwtDTO jwtDTO=SecurityUtil.hasRole(request,null);
        return ResponseEntity.ok(commentLikeService.remove(jwtDTO.getId(),commentId));
    }
}
