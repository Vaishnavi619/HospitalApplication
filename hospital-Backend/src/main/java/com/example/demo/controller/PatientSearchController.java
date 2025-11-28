package com.example.demo.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.PatientIndex;
import com.example.demo.service.PatientSearchService;

@RestController
@RequestMapping("/api/patients/search")
public class PatientSearchController {

    private final PatientSearchService searchService;

    public PatientSearchController(PatientSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN','DOCTOR')")
    public List<PatientIndex> searchByName(@PathVariable String name) {
        return searchService.searchByName(name);
    }

    @GetMapping("/phone/{phone}")
    @PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN','DOCTOR')")
    public List<PatientIndex> searchByPhone(@PathVariable String phone) {
        return searchService.searchByPhone(phone);
    }

    @GetMapping("/address/{address}")
    @PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN','DOCTOR')")
    public List<PatientIndex> searchByAddress(@PathVariable String address) {
        return searchService.searchByAddress(address);
    }
}
