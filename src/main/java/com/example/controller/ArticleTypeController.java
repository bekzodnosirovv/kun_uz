package com.example.controller;

import com.example.dto.ArticleTypeDTO;
import com.example.dto.JwtDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.ArticleTypeService;
import com.example.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/articleType")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;

    @PostMapping(value = "/admin")
    public ResponseEntity<?> create(@Valid @RequestBody ArticleTypeDTO dto,
                                    HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.create(jwtDTO.getId(),dto));
    }

    @PutMapping(value = "/admin/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id,
                                   @Valid @RequestBody ArticleTypeDTO dto,
                                    HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        articleTypeService.update(id, dto);
        return ResponseEntity.ok("Article type update !!!");
    }

    @DeleteMapping(value = "/admin/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                    HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        articleTypeService.delete(id);
        return ResponseEntity.ok("Article type deleted !!!");
    }

    @GetMapping(value = "/admin/all/page")
    public ResponseEntity<?> getAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size,
                                    HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.getAll(page - 1, size));
    }

    @GetMapping(value = "/lan")
    public ResponseEntity<?> getByLan(@RequestParam("lan") Language lan) {
        return ResponseEntity.ok(articleTypeService.getByLan(lan));
    }


}
