package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.SlotDTO;
import com.example.demo.entity.DoctorAvailability;
import com.example.demo.service.DoctorAvailabilityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/doctor-availability")
@RequiredArgsConstructor
public class DoctorAvailabilityController {
	@Autowired
    private  DoctorAvailabilityService availabilityService;

    @PostMapping("/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR','RECEPTIONIST','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<DoctorAvailability> addAvailability(
            @PathVariable int doctorId,
            @RequestBody DoctorAvailability availability) {
        return ResponseEntity.ok(availabilityService.addAvailability(doctorId, availability));
    }

    @GetMapping("/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR','RECEPTIONIST','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<DoctorAvailability>> getAvailability(@PathVariable int doctorId) {
        return ResponseEntity.ok(availabilityService.getAvailabilityByDoctor(doctorId));
    }

    @PutMapping("/{availabilityId}")
    @PreAuthorize("hasAnyRole('DOCTOR','RECEPTIONIST','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<DoctorAvailability> updateAvailability(
            @PathVariable int availabilityId,
            @RequestBody DoctorAvailability availability) {
        return ResponseEntity.ok(availabilityService.updateAvailability(availabilityId, availability));
    }

    @DeleteMapping("/{availabilityId}")
    @PreAuthorize("hasAnyRole('DOCTOR','RECEPTIONIST','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<Void> deleteAvailability(@PathVariable int availabilityId) {
        availabilityService.deleteAvailability(availabilityId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{doctorId}/slots")
    @PreAuthorize("hasAnyRole('DOCTOR','RECEPTIONIST','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<SlotDTO>> getAvailableSlots(
            @PathVariable int doctorId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(availabilityService.getAvailableSlots(doctorId, date));
    }
}
