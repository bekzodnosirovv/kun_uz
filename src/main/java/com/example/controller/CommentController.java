package com.example.controller;

import com.example.dto.CommentDTO;
import com.example.dto.CommentFilterDTO;
import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.service.CommentService;
import com.example.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping(value = "closed")
    public ResponseEntity<?> create(@RequestBody CommentDTO dto,
                                    HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, (ProfileRole) null);
        return ResponseEntity.ok(commentService.create(jwtDTO.getId(), dto));
    }

    @PutMapping(value = "/closed/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String commentId,
                                    @RequestBody CommentDTO dto,
                                    HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, (ProfileRole) null);
        return ResponseEntity.ok(commentService.update(commentId, jwtDTO.getId(), dto));
    }

    @DeleteMapping(value = "/closed/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String commentId,
                                    HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, (ProfileRole) null);
        commentService.delete(jwtDTO, commentId);
        return ResponseEntity.ok("Deleted !!!");
    }

    @GetMapping(value = "/getList/{id}")
    public ResponseEntity<?> getListByArticleId(@PathVariable("id") String articleId) {
        return ResponseEntity.ok(commentService.getListByArticleId(articleId));
    }

    @GetMapping(value = "/closed/get/pagination")
    public ResponseEntity<?> getPagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size,
                                           HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(commentService.getPagination(page-1, size));
    }

    @GetMapping(value = "/closed/filter")
    public ResponseEntity<?> filter(@RequestParam("page") Integer page,
                                    @RequestParam("size") Integer size,
                                    @RequestBody CommentFilterDTO filterDTO,
                                    HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(commentService.filter(page-1, size, filterDTO));
    }

    @GetMapping(value = "/replyList/{id}")
    public ResponseEntity<?> getReplyCommentList(@PathVariable("id") String commentId) {
        return ResponseEntity.ok(commentService.replyList(commentId));
    }
}
