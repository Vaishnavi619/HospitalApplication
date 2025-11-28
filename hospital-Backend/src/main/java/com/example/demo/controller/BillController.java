package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Bill;
import com.example.demo.service.BillService;
import com.example.demo.utility.ResponseStructure;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "http://localhost:4200")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }


    @PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN')")
	@PostMapping("/{patientId}/{prescriptionId}")
	public ResponseEntity<ResponseStructure<Bill>> saveBill( @PathVariable int patientId, @PathVariable int prescriptionId, @RequestBody Map<String, Double> requestConsultationFee) {

	    double consultationFee = requestConsultationFee.get("consultationFee");
	    return billService.saveBill(patientId, prescriptionId, consultationFee);
	}
	
	@PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN')")
	@GetMapping("/{billId}")
	public ResponseEntity<ResponseStructure<Bill>> getBillById(@PathVariable   int billId){
		return billService.getBillById(billId);
	}
	
	@PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN')")
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Bill>>> getAllBills() {
		return billService.getAllBills();
	}
	
	@PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN')")
	@DeleteMapping("/{billId}")
	public ResponseEntity<ResponseStructure<Bill>> deleteBillById(@PathVariable  int billId){
		return billService.deleteBillById(billId);
	}
	
	@PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN')")
	@PutMapping("/{billId}")
	public ResponseEntity<ResponseStructure<Bill>> updateBill(@PathVariable int billId,@RequestBody  Bill updatedBillData){
		return billService.updateBill(billId, updatedBillData);
	}
	@PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN')")
	@GetMapping("/patient/view-bills")
	public ResponseEntity<ResponseStructure<List<Bill>>> getBillsForLoggedInPatient() {
	    return billService.getBillsForLoggedInPatient();
	}
	
}
