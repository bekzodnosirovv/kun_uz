package com.example.controller;


import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.ProfileFilterDTO;
import com.example.enums.ProfileRole;
import com.example.service.ProfileService;
import com.example.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String authToken,
                                    @RequestBody ProfileDTO dto) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(authToken, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.create(jwtDTO, dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String authToken,
                                    @PathVariable("id") Integer id,
                                    @RequestBody ProfileDTO dto) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(authToken, ProfileRole.ADMIN);
        profileService.update(id, dto);
        return ResponseEntity.ok("Update profile !!!");
    }

    @PutMapping("/detail/{id}")
    public ResponseEntity<?> updateProfileDetail(@RequestHeader("Authorization") String authToken,
                                                 @RequestBody ProfileDTO dto) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(authToken, ProfileRole.USER);
        profileService.updateProfileDetail(jwtDTO.getId(), dto);
        return ResponseEntity.ok("Update profile detail !!!");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String authToken) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(authToken, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String authToken,
                                    @PathVariable("id") Integer id) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(authToken, ProfileRole.ADMIN);
        profileService.delete(id);
        return ResponseEntity.ok("Deleted profile !!!");
    }

    @PutMapping("/update/photo")
    public ResponseEntity<?> updatePhoto(@RequestHeader("Authorization") String authToken) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(authToken, ProfileRole.ADMIN);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(@RequestHeader("Authorization") String authToken,
                                    @RequestBody ProfileFilterDTO filterDTO) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(authToken, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.filter(filterDTO));
    }
}
