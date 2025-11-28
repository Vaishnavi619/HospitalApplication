package com.example.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Medicine;
import com.example.demo.utility.ResponseStructure;

public interface MedicineService { 
	public ResponseEntity<ResponseStructure<Medicine>>  saveMedicine(Medicine medicine);
	public ResponseEntity<ResponseStructure<Medicine>>  getMedicineById(int medicineId);
	public ResponseEntity<ResponseStructure<List<Medicine>>> getAllMedicine();
	public ResponseEntity<ResponseStructure<Medicine>> deleteMedicineById(int medicineId);
	public ResponseEntity<ResponseStructure<Medicine>> updateMedicine(int medicineId,Medicine medicine);
	public ResponseEntity<ResponseStructure<String>>  saveMedicinesFromCsv(MultipartFile file);

}
