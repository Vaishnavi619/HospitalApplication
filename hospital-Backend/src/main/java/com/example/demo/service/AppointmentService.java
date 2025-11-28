package com.example.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.demo.ennums.AppointmentStatus;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.utility.ResponseStructure;

public interface AppointmentService {
		public ResponseEntity<ResponseStructure<Appointment>>  saveAppointment(Appointment appointment);
		public ResponseEntity<ResponseStructure<Appointment>>  getAppointmentById(int appointmentId);
		public ResponseEntity<ResponseStructure<List<Appointment>>> getAllAppointments();
		public ResponseEntity<ResponseStructure<Appointment>> deleteAppointmentById(int appointmentId);
		public ResponseEntity<ResponseStructure<Appointment>> updateAppointment(int appointmentId,Appointment appointment);
		public List<Appointment> getAppointmentsByDoctor(Doctor doctor);
		public ResponseEntity<ResponseStructure<Appointment>> updateAppointmentStatus(int appointmentId, AppointmentStatus status);
		
		public Appointment cancelAppointment(int appointmentId, int loggedInUserId);



}
