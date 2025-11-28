package com.example.demo.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.ennums.PaymentStatus;
import com.example.demo.entity.Bill;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Prescription;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.BillRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.PrescriptionRepository;
import com.example.demo.service.BillService;
import com.example.demo.service.PatientService;
import com.example.demo.utility.ResponseStructure;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class BillServiceImpl implements BillService{
	
	@Autowired
	private BillRepository billRepository;
	
	@Autowired
	private PatientRepository patientRepository;
	
	@Autowired
	private PrescriptionRepository prescriptionRepository;
	@Autowired
	private PatientService patientService;
	@Override
	public ResponseEntity<ResponseStructure<Bill>> saveBill(int patientId, int prescriptionId, double consultationFee) {

	    // ✅ 1. Fetch Patient
	    Patient patient = patientRepository.findById(patientId)
	            .orElseThrow(() -> new UserNotFoundException("Patient not found"));

	    // ✅ 2. Fetch Prescription
	    Prescription prescription = prescriptionRepository.findById(prescriptionId)
	            .orElseThrow(() -> new UserNotFoundException("Prescription not found"));

	    // ✅ 3. Initialize medicine charges
	    double totalMedicineCharges = 0.0;

	    // ✅ 4. Extract and parse medicinesJson
	    if (prescription.getMedicinesJson() != null && !prescription.getMedicinesJson().isBlank()) {
	        try {
	            ObjectMapper mapper = new ObjectMapper();
	            List<Map<String, Object>> medicines = mapper.readValue(
	                    prescription.getMedicinesJson(),
	                    new TypeReference<List<Map<String, Object>>>() {}
	            );

	            for (Map<String, Object> med : medicines) {
	                // safely handle missing fields
	                double price = 0.0;
	                if (med.get("price") != null) {
	                    try {
	                        price = Double.parseDouble(med.get("price").toString());
	                    } catch (NumberFormatException e) {
	                        price = 0.0;
	                    }
	                }
	                totalMedicineCharges += price;
	            }

	        } catch (Exception e) {
	            throw new RuntimeException("Error reading medicines JSON for prescription ID: " + prescriptionId, e);
	        }
	    }

	    // ✅ 5. Create Bill object
	    Bill bill = new Bill();
	    bill.setPatient(patient);
	    bill.setPrescription(prescription);
	    bill.setConsultationFee(consultationFee);
	    bill.setMedicineCharges(totalMedicineCharges);
	    bill.setTotalAmount(totalMedicineCharges + consultationFee);
	    bill.setBillDate(LocalDate.now());
	    bill.setPaymentStatus(PaymentStatus.UNPAID); // default

	    // ✅ 6. Save Bill
	    Bill savedBill = billRepository.save(bill);

	    // ✅ 7. Prepare response
	    ResponseStructure<Bill> response = new ResponseStructure<>();
	    response.setStatuscode(HttpStatus.OK.value());
	    response.setMessage("✅ Bill generated successfully!");
	    response.setData(savedBill);

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}



	private int extractDosageNumber(String input) {
	    if (input == null || input.trim().isEmpty()) {
	        return 1; // default
	    }
	    try {
	        return Integer.parseInt(input.replaceAll("[^0-9]", ""));
	    } catch (NumberFormatException e) {
	        return 1;
	    }
	}





	@Override
	public ResponseEntity<ResponseStructure<Bill>> getBillById(int billId) {
	    Optional<Bill> optional = billRepository.findById(billId);
	    
	    if (optional.isEmpty()) {
	        throw new UserNotFoundException("Bill Not Found");
	    } else {
	        Bill bill = optional.get();
	        
	        ResponseStructure<Bill> responseStructure = new ResponseStructure<>();
	        responseStructure.setStatuscode(HttpStatus.OK.value());
	        responseStructure.setMessage("Bill object found by Id");
	        responseStructure.setData(bill);
	        
	        return new ResponseEntity<ResponseStructure<Bill>>(responseStructure, HttpStatus.OK);
	    }
	}


	@Override
	public ResponseEntity<ResponseStructure<List<Bill>>> getAllBills() {
	    List<Bill> bills = billRepository.findAll();

	    ResponseStructure<List<Bill>> responseStructure = new ResponseStructure<>();
	    
	    if (bills.isEmpty()) {
	        responseStructure.setStatuscode(HttpStatus.NOT_FOUND.value());
	        responseStructure.setMessage("No bills found");
	        responseStructure.setData(bills);
	        return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
	    } else {
	        responseStructure.setStatuscode(HttpStatus.OK.value());
	        responseStructure.setMessage("List of all bills retrieved successfully");
	        responseStructure.setData(bills);
	        return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	    }
	}


	@Override
	public ResponseEntity<ResponseStructure<Bill>> deleteBillById(int billId) {
	    Optional<Bill> optional = billRepository.findById(billId);

	    if (optional.isEmpty()) {
	        throw new UserNotFoundException("Bill not found with ID: " + billId);
	    } else {
	        Bill bill = optional.get();
	        billRepository.delete(bill);

	        ResponseStructure<Bill> responseStructure = new ResponseStructure<>();
	        responseStructure.setStatuscode(HttpStatus.OK.value());
	        responseStructure.setMessage("Bill deleted successfully");
	        responseStructure.setData(bill);

	        return new ResponseEntity<ResponseStructure<Bill>>(responseStructure, HttpStatus.OK);
	    }
	}

	@Override
	public ResponseEntity<ResponseStructure<Bill>> updateBill(int billId, Bill updatedBillData) {
	    // ✅ 1. Fetch Bill
	    Bill existingBill = billRepository.findById(billId)
	            .orElseThrow(() -> new UserNotFoundException("Bill not found with ID: " + billId));

	    // ✅ 2. Update consultation fee
	    existingBill.setConsultationFee(updatedBillData.getConsultationFee());

	    // ✅ 3. Fetch prescription linked to this bill
	    Prescription prescription = existingBill.getPrescription();
	    double totalMedicineCharges = 0.0;

	    // ✅ 4. If prescription has medicinesJson, parse and sum prices
	    if (prescription != null && prescription.getMedicinesJson() != null && !prescription.getMedicinesJson().isBlank()) {
	        try {
	            ObjectMapper mapper = new ObjectMapper();
	            List<Map<String, Object>> medicines = mapper.readValue(
	                    prescription.getMedicinesJson(),
	                    new TypeReference<List<Map<String, Object>>>() {}
	            );

	            for (Map<String, Object> med : medicines) {
	                if (med.get("price") != null) {
	                    try {
	                        totalMedicineCharges += Double.parseDouble(med.get("price").toString());
	                    } catch (NumberFormatException e) {
	                        // Skip invalid price values
	                    }
	                }
	            }

	        } catch (Exception e) {
	            throw new RuntimeException("Error parsing medicines JSON for prescription ID: " + prescription.getId(), e);
	        }
	    }

	    // ✅ 5. Update charges
	    existingBill.setMedicineCharges(totalMedicineCharges);
	    existingBill.setTotalAmount(totalMedicineCharges + existingBill.getConsultationFee());

	    // ✅ 6. Save the updated bill
	    Bill updatedBill = billRepository.save(existingBill);

	    // ✅ 7. Prepare and return response
	    ResponseStructure<Bill> responseStructure = new ResponseStructure<>();
	    responseStructure.setStatuscode(HttpStatus.OK.value());
	    responseStructure.setMessage("✅ Bill updated successfully!");
	    responseStructure.setData(updatedBill);

	    return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	
	@Override
	public ResponseEntity<ResponseStructure<List<Bill>>> getBillsForLoggedInPatient() {
		ResponseEntity<ResponseStructure<Patient>> response = patientService.getLoggedInPatient();
		Patient patient = response.getBody().getData(); 


	    List<Bill> bills = billRepository.findByPatient(patient);

	    ResponseStructure<List<Bill>> response1 = new ResponseStructure<>();
	    response1.setStatuscode(HttpStatus.OK.value());
	    response1.setMessage("Bills for logged-in patient");
	    response1.setData(bills);

	    return new ResponseEntity<>(response1, HttpStatus.OK);
	}


}
