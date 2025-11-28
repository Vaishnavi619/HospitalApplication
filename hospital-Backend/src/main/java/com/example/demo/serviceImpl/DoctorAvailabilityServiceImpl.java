package com.example.demo.serviceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.SlotDTO;
import com.example.demo.ennums.AppointmentStatus;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.DoctorAvailability;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorAvailabilityRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.service.DoctorAvailabilityService;

@Service
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Override
    public DoctorAvailability addAvailability(int doctorId, DoctorAvailability availability) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        availability.setDoctor(doctor);
        return availabilityRepository.save(availability);
    }

    @Override
    public List<DoctorAvailability> getAvailabilityByDoctor(int doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return availabilityRepository.findByDoctor(doctor);
    }

    @Override
    public DoctorAvailability updateAvailability(int availabilityId, DoctorAvailability updated) {
        DoctorAvailability existing = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        existing.setDayOfWeek(updated.getDayOfWeek());
        existing.setStartTime(updated.getStartTime());
        existing.setEndTime(updated.getEndTime());

        return availabilityRepository.save(existing);
    }

    @Override
    public void deleteAvailability(int availabilityId) {
        if (!availabilityRepository.existsById(availabilityId)) {
            throw new RuntimeException("Availability not found");
        }
        availabilityRepository.deleteById(availabilityId);
    }

    @Override
    public List<SlotDTO> getAvailableSlots(int doctorId, LocalDate date) {
        final int SLOT_MINUTES = 30;

        // Get doctor
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Get doctor availability for this day
        DayOfWeek dayEnum = date.getDayOfWeek();
        List<DoctorAvailability> availabilities =
                availabilityRepository.findByDoctorAndDayOfWeek(doctor, dayEnum);

        // Get appointments EXCLUDING CANCELLED ones
        List<AppointmentStatus> activeStatuses = List.of(
                AppointmentStatus.APPROVED,
                AppointmentStatus.PENDING,
                AppointmentStatus.BOOKED
        );

        List<Appointment> appointments =
                appointmentRepository.findByDoctorDoctorIdAndAppointmentDateAndStatusIn(
                        doctorId, date, activeStatuses
                );

        List<SlotDTO> freeSlots = new ArrayList<>();

        // Loop through availability
        for (DoctorAvailability availability : availabilities) {
            LocalTime start = availability.getStartTime();
            LocalTime end = availability.getEndTime();

            while (start.isBefore(end)) {
                LocalTime slotEnd = start.plusMinutes(SLOT_MINUTES);
                if (slotEnd.isAfter(end)) break;

                // Ignore past times for today's date
                if (date.equals(LocalDate.now()) && slotEnd.isBefore(LocalTime.now())) {
                    start = slotEnd;
                    continue;
                }

                boolean overlaps = false;

                for (Appointment appt : appointments) {

                    LocalTime apptStart;
                    LocalTime apptEnd;

                    // Some appointments use custom startTime/endTime
                    if (appt.getStartTime() != null && appt.getEndTime() != null) {
                        apptStart = appt.getStartTime();
                        apptEnd = appt.getEndTime();
                    }
                    // Some use appointmentTime only
                    else {
                        apptStart = appt.getAppointmentTime();
                        apptEnd = apptStart.plusMinutes(SLOT_MINUTES);
                    }

                    // Check overlap
                    if (apptStart.isBefore(slotEnd) && apptEnd.isAfter(start)) {
                        overlaps = true;
                        break;
                    }
                }

                // If no overlap, slot is free
                if (!overlaps) {
                    freeSlots.add(new SlotDTO(start, slotEnd));
                }

                start = slotEnd;
            }
        }

        return freeSlots;
    }


}

