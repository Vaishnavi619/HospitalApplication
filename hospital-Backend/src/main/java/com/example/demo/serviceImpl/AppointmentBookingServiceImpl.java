package com.example.demo.serviceImpl;

import com.example.demo.dto.AppointmentBookingDto;
import com.example.demo.dto.AvailableSlotDto;
import com.example.demo.dto.SomeDoctorSummaryDto;
import com.example.demo.ennums.AppointmentStatus;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.DoctorAvailability;
import com.example.demo.entity.Patient;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorAvailabilityRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.service.AppointmentBookingService;
import com.example.demo.utility.ResponseStructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentBookingServiceImpl implements AppointmentBookingService {

    @Autowired private DoctorRepository doctorRepository;
    @Autowired private DoctorAvailabilityRepository availabilityRepository;
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private PatientRepository patientRepository;

    private static final Duration SLOT_DURATION = Duration.ofMinutes(30);

    @Override
    public ResponseEntity<ResponseStructure<List<AvailableSlotDto>>> getAvailableSlots(int doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        DayOfWeek dow = date.getDayOfWeek();

        // fetch availability windows for that doctor & day
        List<DoctorAvailability> windows = availabilityRepository.findByDoctorDoctorIdAndDayOfWeek(doctorId, dow);
        List<LocalTime> candidateSlots = new ArrayList<>();

        for (DoctorAvailability w : windows) {
            LocalTime start = w.getStartTime();
            LocalTime end = w.getEndTime();

            // generate slots: inclusive start, exclusive end
            LocalTime cur = start;
            while (!cur.isAfter(end.minus(SLOT_DURATION))) { // ensure last slot starts before end
                candidateSlots.add(cur);
                cur = cur.plus(SLOT_DURATION);
            }
        }

        // remove duplicates & sort
        List<LocalTime> allSlots = candidateSlots.stream().distinct().sorted().collect(Collectors.toList());

        // get booked times for that doctor on the date
        List<Appointment> booked = appointmentRepository.findByDoctorDoctorIdAndAppointmentDate(doctorId, date);
        Set<LocalTime> bookedTimes = booked.stream()
                .map(app -> app.getAppointmentTime()) // adapt if field name is different
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<AvailableSlotDto> available = allSlots.stream()
                .filter(t -> !bookedTimes.contains(t))
                .map(t -> {
                    AvailableSlotDto s = new AvailableSlotDto();

                    LocalTime start = t;
                    LocalTime end   = t.plus(SLOT_DURATION);

                    s.setStartTime(start.toString());     // "16:00:00"
                    s.setEndTime(end.toString());         // "16:30:00"
                    s.setSlot(start.toString());          // "16:00"
                    s.setSlotTime(start.toString());      // "16:00:00"

                    String display = start.format(DateTimeFormatter.ofPattern("hh:mm a")).toLowerCase();
                    s.setDisplay(display);                // "04:00 pm"

                    return s;
                })

                .collect(Collectors.toList());

        ResponseStructure<List<AvailableSlotDto>> r = new ResponseStructure<>();
        r.setStatuscode(HttpStatus.OK.value());
        r.setMessage("Available slots fetched");
        r.setData(available);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseStructure<List<SomeDoctorSummaryDto>>> getDoctorsWithSlots() {
        List<Doctor> doctors = doctorRepository.findAll();

        List<SomeDoctorSummaryDto> list = new ArrayList<>();

        LocalDate today = LocalDate.now();
        DayOfWeek todayDay = today.getDayOfWeek();

        for (Doctor doctor : doctors) {

            // Fetch doctor availability
            List<DoctorAvailability> availabilityList =
                    availabilityRepository.findByDoctorDoctorId(doctor.getDoctorId());

            // Filter ONLY today's availability
            List<DoctorAvailability> todaysAvailability = availabilityList.stream()
                    .filter(a -> a.getDayOfWeek().equals(todayDay))
                    .toList();

            List<AvailableSlotDto> slots = new ArrayList<>();

            for (DoctorAvailability d : todaysAvailability) {
            	slots.addAll(generateSlots(d.getStartTime(), d.getEndTime()));

            }

            // Create response DTO
            SomeDoctorSummaryDto dto = new SomeDoctorSummaryDto();
            dto.setDoctorId(doctor.getDoctorId());
            dto.setDoctorName(doctor.getDoctorName());
            dto.setSpecialization(doctor.getSpecialization());
            dto.setTodayAvailableSlots(slots);

            list.add(dto);
        }

        ResponseStructure<List<SomeDoctorSummaryDto>> structure = new ResponseStructure<>();
        structure.setStatuscode(200);
        structure.setMessage("Doctors fetched");
        structure.setData(list);

        return ResponseEntity.ok(structure);
    }


    @Override
    public ResponseEntity<ResponseStructure<Appointment>> bookAppointment(AppointmentBookingDto dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        LocalDate date = LocalDate.parse(dto.getAppointmentDate());
        LocalTime time = LocalTime.parse(dto.getAppointmentTime());

        // validate that chosen time is within some availability window for that day
        DayOfWeek dow = date.getDayOfWeek();
        List<DoctorAvailability> windows = availabilityRepository.findByDoctorDoctorIdAndDayOfWeek(doctor.getDoctorId(), dow);
        boolean withinWindow = windows.stream().anyMatch(w -> !time.isBefore(w.getStartTime()) && !time.isAfter(w.getEndTime().minus(SLOT_DURATION)));
        if (!withinWindow) {
            throw new RuntimeException("Selected time is outside doctor's availability");
        }

        // check conflict
        boolean exists = appointmentRepository.existsByDoctorDoctorIdAndAppointmentDateAndAppointmentTime(doctor.getDoctorId(), date, time);
        if (exists) {
            throw new RuntimeException("Slot already booked");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(time); // main slot time

        // NEW — set slot start and end
        appointment.setStartTime(time);
        appointment.setEndTime(time.plusMinutes(30)); // because your slot duration = 30 minutes

        appointment.setReason(dto.getReason());
        appointment.setStatus(AppointmentStatus.APPROVED);



        Appointment saved = appointmentRepository.save(appointment);

        ResponseStructure<Appointment> r = new ResponseStructure<>();
        r.setStatuscode(HttpStatus.OK.value());
        r.setMessage("Appointment booked successfully");
        r.setData(saved);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    private static String formatLocalTime(LocalTime t) {
        return t.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }
    
    
    private List<AvailableSlotDto> generateSlots(LocalTime startTime, LocalTime endTime) {

        List<AvailableSlotDto> slots = new ArrayList<>();

        int duration = 30; // since your output shows 30-minute gaps

        LocalTime current = startTime;

        while (!current.isAfter(endTime.minusMinutes(duration))) {

            LocalTime slotStart = current;
            LocalTime slotEnd = current.plusMinutes(duration);

            AvailableSlotDto dto = new AvailableSlotDto();

            // Save string values
            dto.setStartTime(slotStart.toString());      // "16:00:00"
            dto.setEndTime(slotEnd.toString());          // "16:30:00"

            dto.setSlot(slotStart.toString());           // "16:00"
            dto.setSlotTime(slotStart.toString());       // "16:00:00"

            String displayTime = slotStart.format(DateTimeFormatter.ofPattern("hh:mm a")).toLowerCase();
            dto.setDisplay(displayTime);                 // "04:00 pm"

            slots.add(dto);

            current = slotEnd;
        }

        return slots;
    }

}
