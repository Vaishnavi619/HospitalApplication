package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.ennums.AppointmentStatus;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
	@Query("SELECT a FROM Appointment a JOIN FETCH a.doctor JOIN FETCH a.patient")
    List<Appointment> findAllAppointmentsWithDetails();

    boolean existsByDoctorDoctorId(int doctorId);

    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByPatient(Patient patient);

    @Query("SELECT FUNCTION('MONTHNAME', a.appointmentDate), COUNT(a) " +
           "FROM Appointment a GROUP BY FUNCTION('MONTHNAME', a.appointmentDate)")
    List<Object[]> countAppointmentsPerMonth();

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
           "FROM Appointment a " +
           "WHERE a.doctor.doctorId = :doctorId " +
           "AND a.appointmentDate = :date " +
           "AND (a.startTime < :endTime AND a.endTime > :startTime)")
    boolean existsOverlappingAppointment(
            @Param("doctorId") int doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("SELECT a.appointmentTime FROM Appointment a " +
           "WHERE a.doctor.doctorId = :doctorId " +
           "AND a.appointmentDate = :date")
    List<LocalTime> findBookedTimeSlots(
            @Param("doctorId") int doctorId,
            @Param("date") LocalDate date
    );

    List<Appointment> findByDoctorDoctorIdAndAppointmentDate(int doctorId, LocalDate appointmentDate);
 

    // exists check used to prevent double-booking
    boolean existsByDoctorDoctorIdAndAppointmentDateAndAppointmentTime(int doctorId, LocalDate appointmentDate, LocalTime appointmentTime);
    
    Optional<Appointment> findByDoctorAndAppointmentDateAndStartTimeAndStatus(
            Doctor doctor, LocalDate appointmentDate, LocalTime startTime, AppointmentStatus status);


    List<Appointment> findByDoctorDoctorIdAndAppointmentDateAndStatusNot(
            int doctorId, LocalDate date, AppointmentStatus status);

    List<Appointment> findByDoctorDoctorIdAndAppointmentDateAndStatusIn(
            int doctorId,
            LocalDate appointmentDate,
            List<AppointmentStatus> statuses
    );


}
