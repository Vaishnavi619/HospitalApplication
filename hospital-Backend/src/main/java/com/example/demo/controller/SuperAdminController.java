package com.example.demo.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/superadmin")
public class SuperAdminController {

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<String> getSuperAdminData() {
        return ResponseEntity.ok("👑 Hello Super Admin, welcome to your dashboard!");
    }
}
