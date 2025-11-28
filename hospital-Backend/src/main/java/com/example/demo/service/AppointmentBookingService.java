package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.demo.dto.AppointmentBookingDto;
import com.example.demo.dto.AvailableSlotDto;
import com.example.demo.dto.SomeDoctorSummaryDto;
import com.example.demo.entity.Appointment;
import com.example.demo.utility.ResponseStructure;

public interface AppointmentBookingService {
	ResponseEntity<ResponseStructure<List<AvailableSlotDto>>> getAvailableSlots(int doctorId, LocalDate date);
    ResponseEntity<ResponseStructure<List<SomeDoctorSummaryDto>>> getDoctorsWithSlots();
    ResponseEntity<ResponseStructure<Appointment>> bookAppointment(AppointmentBookingDto dto);
    
}
