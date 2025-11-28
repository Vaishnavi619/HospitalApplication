package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Patient;

public interface BillRepository extends JpaRepository<Bill, Integer> {
	List<Bill> findByPatient(Patient patient);
	Optional<Bill> findByBillId(int billId);
	   @Query("SELECT b.paymentStatus, COUNT(b) FROM Bill b GROUP BY b.paymentStatus")
	    List<Object[]> countByPaymentStatus();

}
