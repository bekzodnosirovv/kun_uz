package com.example.controller;

import com.example.dto.JwtDTO;
import com.example.dto.RegionDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.RegionService;
import com.example.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/region")
public class RegionController {
    @Autowired
    private RegionService regionService;


    @PostMapping("")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String authToken,
                                    @RequestBody RegionDTO dto) {

        return ResponseEntity.ok(regionService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String authToken,
                                    @PathVariable("id") Integer id,
                                    @RequestBody RegionDTO dto) {

        regionService.update(id, dto);
        return ResponseEntity.ok("Region update !!!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String authToken,
                                    @PathVariable("id") Integer id) {

        regionService.delete(id);
        return ResponseEntity.ok("Region deleted !!!");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String authToken) {

        return ResponseEntity.ok(regionService.getAll());
    }

    @GetMapping("/lan")
    public ResponseEntity<?> getByLan(@RequestHeader("Authorization") String authToken,
                                      @RequestParam("lan") Language lan) {
        return ResponseEntity.ok(regionService.getByLan(lan));
    }

}
