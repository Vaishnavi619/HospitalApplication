package com.example.demo.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Medicine;
import com.example.demo.service.MedicineService;
import com.example.demo.utility.ResponseStructure;

@RestController
@RequestMapping("/api/medicines")
@CrossOrigin(origins = "http://localhost:4200")
public class MedicineController {
    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }
		@PreAuthorize("hasAnyRole('ADMIN','DOCTOR','SUPER_ADMIN','RECEPTIONIST')")
		@PostMapping
		public ResponseEntity<ResponseStructure<Medicine>>  saveMedicine(@RequestBody Medicine medicine){
			return medicineService.saveMedicine(medicine);
		}
		
		@PreAuthorize("hasAnyRole('ADMIN','DOCTOR','SUPER_ADMIN','RECEPTIONIST')")
		@GetMapping("/{medicineId}")
		public ResponseEntity<ResponseStructure<Medicine>>  getMedicineById(@PathVariable int medicineId){
			return medicineService.getMedicineById(medicineId);
		}
		
		@PreAuthorize("hasAnyRole('ADMIN','DOCTOR','SUPER_ADMIN','RECEPTIONIST')")
		@GetMapping
		public ResponseEntity<ResponseStructure<List<Medicine>>> getAllMedicine(){
			return medicineService.getAllMedicine();
		}
		
		@PreAuthorize("hasAnyRole('ADMIN','DOCTOR','SUPER_ADMIN','RECEPTIONIST')")
		@DeleteMapping("/{medicineId}")
		public ResponseEntity<ResponseStructure<Medicine>>  deleteMedicineById(@PathVariable int medicineId){
			return medicineService.deleteMedicineById(medicineId);
		}
		
		@PreAuthorize("hasAnyRole('ADMIN','DOCTOR','SUPER_ADMIN','RECEPTIONIST')")
		@PutMapping("/{medicineId}")
		public ResponseEntity<ResponseStructure<Medicine>> updateMedicine(@PathVariable int medicineId,@RequestBody Medicine medicine){
			return medicineService.updateMedicine(medicineId, medicine);
		}
		
		@PostMapping("/upload-medicines")
		@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','RECEPTIONIST')")
		public ResponseEntity<ResponseStructure<String>> uploadMedicinesCSV(@RequestParam("file") MultipartFile file)  {
		    return medicineService.saveMedicinesFromCsv(file);
		}


}
