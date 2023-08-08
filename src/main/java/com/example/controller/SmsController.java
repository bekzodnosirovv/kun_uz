package com.example.controller;

import com.example.enums.ProfileRole;
import com.example.service.SmsHistoryService;
import com.example.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
@RestController
@RequestMapping("/api/v1/sm/history")
public class SmsController {
    @Autowired
    private SmsHistoryService smsHistoryService;

    @GetMapping(value = "/{phone}")
    public ResponseEntity<?> get(@PathVariable("phone") String phone,
                                 HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ROLE_ADMIN);
        return ResponseEntity.ok(smsHistoryService.get(phone));
    }

    @GetMapping(value = "/date")
    public ResponseEntity<?> getByDate(@RequestParam("date") LocalDate date,
                                       HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ROLE_ADMIN);
        return ResponseEntity.ok(smsHistoryService.getByDate(date));
    }

    @GetMapping(value = "/page")
    public ResponseEntity<?> getPagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size,
                                           HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ROLE_ADMIN);
        return ResponseEntity.ok(smsHistoryService.pagination(page - 1, size));
    }
}
