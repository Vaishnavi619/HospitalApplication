package com.example.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Doctor;
import com.example.demo.entity.DoctorAvailability;

import java.time.DayOfWeek;

import java.util.List;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Integer> {
	List<DoctorAvailability> findByDoctor(Doctor doctor);
    List<DoctorAvailability> findByDoctorAndDayOfWeek(Doctor doctor, DayOfWeek dayOfWeek);
    List<DoctorAvailability> findByDoctorDoctorId(int doctorId);
    List<DoctorAvailability> findByDoctorDoctorIdAndDayOfWeek(int doctorId, DayOfWeek dayOfWeek);
}
