package com.example.controller;

import com.example.enums.ProfileRole;
import com.example.service.EmailHistoryService;
import com.example.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/email/history")
public class EmailHistoryController {

    @Autowired
    private EmailHistoryService emailHistoryService;

    @GetMapping(value = "/{email}")
    public ResponseEntity<?> get(@PathVariable("email") String email,
                                 HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailHistoryService.get(email));
    }

    @GetMapping(value = "/date")
    public ResponseEntity<?> getByDate(@RequestParam("date") LocalDate date,
                                       HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailHistoryService.getByDate(date));
    }

    @GetMapping(value = "/page")
    public ResponseEntity<?> getPagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size,
                                           HttpServletRequest request) {
        SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailHistoryService.pagination(page - 1, size));
    }
}
