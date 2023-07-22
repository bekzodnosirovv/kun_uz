package com.example.controller;

import com.example.dto.JwtDTO;
import com.example.dto.RegionDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.RegionService;
import com.example.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/region")
public class RegionController {
    @Autowired
    private RegionService regionService;


    @PostMapping("/admin")
    public ResponseEntity<?> create(@RequestBody RegionDTO dto,
                                    HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.create(dto));
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id,
                                    @RequestBody RegionDTO dto,
                                    HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        regionService.update(id, dto);
        return ResponseEntity.ok("Region update !!!");
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                    HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        regionService.delete(id);
        return ResponseEntity.ok("Region deleted !!!");
    }

    @GetMapping("/admin/all")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.getAll());
    }

    @GetMapping("/lan")
    public ResponseEntity<?> getByLan(@RequestParam("lan") Language lan) {
        return ResponseEntity.ok(regionService.getByLan(lan));
    }

}
