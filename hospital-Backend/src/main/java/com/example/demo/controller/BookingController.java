package com.example.demo.controller;

import com.example.demo.dto.AppointmentBookingDto;
import com.example.demo.dto.AvailableSlotDto;
import com.example.demo.entity.Appointment;
import com.example.demo.service.AppointmentBookingService;
import com.example.demo.utility.ResponseStructure;
import com.example.demo.dto.SomeDoctorSummaryDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:4200")
public class BookingController {

    private final AppointmentBookingService bookingService;

    public BookingController(AppointmentBookingService bookingService) {
        this.bookingService = bookingService;
    }
    @GetMapping("/doctors")
    public ResponseEntity<ResponseStructure<List<SomeDoctorSummaryDto>>> getDoctors() {
        return bookingService.getDoctorsWithSlots();
    }

    @GetMapping("/doctors/{doctorId}/available-slots")
    public ResponseEntity<ResponseStructure<List<AvailableSlotDto>>> getAvailableSlots(
            @PathVariable int doctorId,
            @RequestParam String date // yyyy-MM-dd
    ) {
        LocalDate localDate = LocalDate.parse(date);
        return bookingService.getAvailableSlots(doctorId, localDate);
    }

    @PostMapping("/appointments/book")
    public ResponseEntity<ResponseStructure<Appointment>> bookAppointment(@RequestBody AppointmentBookingDto dto) {
        return bookingService.bookAppointment(dto);
    }
}
