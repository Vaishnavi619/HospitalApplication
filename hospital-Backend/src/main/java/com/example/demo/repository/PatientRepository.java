package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Patient;
import com.example.demo.entity.User;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer>{
	Optional<Patient> findByUser(User user);
	Optional<Patient> findByPhone(long phone);
	


}
