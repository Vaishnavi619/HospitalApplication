package com.example.demo.service;

import java.util.List;


import org.springframework.http.ResponseEntity;

import com.example.demo.entity.Bill;
import com.example.demo.utility.ResponseStructure;

public interface BillService {
	ResponseEntity<ResponseStructure<Bill>> saveBill(int patientId, int prescriptionId, double consultationFee);

    ResponseEntity<ResponseStructure<Bill>> getBillById(int billId);

    ResponseEntity<ResponseStructure<List<Bill>>> getAllBills();

    ResponseEntity<ResponseStructure<Bill>> deleteBillById(int billId);

    ResponseEntity<ResponseStructure<Bill>> updateBill(int billId, Bill updatedBillData);
    
    ResponseEntity<ResponseStructure<List<Bill>>> getBillsForLoggedInPatient();
   
}
