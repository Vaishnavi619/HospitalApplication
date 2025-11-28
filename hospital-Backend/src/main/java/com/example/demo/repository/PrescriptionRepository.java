package com.example.demo.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
	List<Prescription> findByAppointmentDoctor(Doctor doctor);
	List<Prescription> findByAppointmentPatient(Patient patient);
	Optional<Prescription> findByAppointment(Appointment appointment);
	List<Prescription> findByPatient(Patient patient);


}
