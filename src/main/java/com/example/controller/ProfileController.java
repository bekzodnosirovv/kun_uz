package com.example.controller;


import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.ProfileFilterDTO;
import com.example.enums.ProfileRole;
import com.example.service.ProfileService;
import com.example.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/closed")
    public ResponseEntity<?> create(@RequestBody ProfileDTO dto,
                                    HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.create(jwtDTO.getId(), dto));
    }

    @PutMapping("/closed/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id,
                                    @RequestBody ProfileDTO dto,
                                    HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok( profileService.update(id, dto));
    }

    @PutMapping("/closed/detail")
    public ResponseEntity<?> updateProfileDetail(@RequestBody ProfileDTO dto,
                                                 HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request,  null);
        profileService.updateProfileDetail(jwtDTO.getId(), dto);
        return ResponseEntity.ok("Update profile detail !!!");
    }

    @GetMapping("/closed/all")
    public ResponseEntity<?> getAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size,
                                    HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.getAll(page - 1, size));
    }

    @DeleteMapping("/closed/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                    HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        profileService.delete(id);
        return ResponseEntity.ok("Deleted profile !!!");
    }

    @PutMapping("/closed/photo/{id}")
    public ResponseEntity<?> updatePhoto(@PathVariable("id") String photoId,
                                         HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, null);
        profileService.updatePhoto(jwtDTO.getId(), photoId);
        return ResponseEntity.ok("Photo update !!!");
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(@RequestBody ProfileFilterDTO filterDTO) {
        return ResponseEntity.ok(profileService.filter(filterDTO));
    }
}
