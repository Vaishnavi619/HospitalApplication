package com.example.demo.controller;



import com.example.demo.service.ReportService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:4200")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @GetMapping("/appointments-per-month")
    public ResponseEntity<List<Map<String, Object>>> getAppointmentsReport() {
        return ResponseEntity.ok(reportService.getAppointmentsPerMonth());
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @GetMapping("/bills-status")
    public ResponseEntity<List<Map<String, Object>>> getBillStatusReport() {
        return ResponseEntity.ok(reportService.getBillStatusCounts());
    }
}
