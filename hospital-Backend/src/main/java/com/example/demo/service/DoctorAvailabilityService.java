package com.example.demo.service;

import com.example.demo.dto.SlotDTO;
import com.example.demo.entity.DoctorAvailability;

import java.time.LocalDate;
import java.util.List;

public interface DoctorAvailabilityService {
   
    DoctorAvailability addAvailability(int doctorId, DoctorAvailability availability);
    List<DoctorAvailability> getAvailabilityByDoctor(int doctorId);
    DoctorAvailability updateAvailability(int availabilityId, DoctorAvailability availability);
    void deleteAvailability(int availabilityId);
    List<SlotDTO> getAvailableSlots(int doctorId, LocalDate date);
    
}

