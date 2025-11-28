package com.example.demo.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import java.util.List;
import java.util.Optional;

import java.io.Reader;
import java.nio.charset.StandardCharsets;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // ✅ Logging
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.MedicineEvent;
import com.example.demo.entity.Medicine;
import com.example.demo.exception.NoDataFoundInDatabaseException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.kafka.MedicineProducer;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.service.MedicineService;
import com.example.demo.utility.ResponseStructure;

@Service
public class MedicineServiceImpl implements MedicineService {

    private static final Logger logger = LoggerFactory.getLogger(MedicineServiceImpl.class); // ✅ Logger

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MedicineProducer medicineProducer;

    @Override
    public ResponseEntity<ResponseStructure<Medicine>> saveMedicine(Medicine medicine) {
        logger.info("Saving new medicine: {}", medicine.getName());

        Medicine medicine1 = medicineRepository.save(medicine);

        logger.debug("Medicine saved successfully with ID: {}", medicine1.getMedicineId());

        ResponseStructure<Medicine> responseStructure = new ResponseStructure<>();
        responseStructure.setStatuscode(HttpStatus.OK.value());
        responseStructure.setMessage("Medicine Added Successfully");
        responseStructure.setData(medicine1);

        return new ResponseEntity<>(responseStructure, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseStructure<Medicine>> getMedicineById(int medicineId) {
        logger.info("Fetching medicine by ID: {}", medicineId);

        Optional<Medicine> optional = medicineRepository.findById(medicineId);
        if (optional.isEmpty()) {
            logger.warn("Medicine not found with ID: {}", medicineId);
            throw new UserNotFoundException("Medicine Not Found");
        } else {
            logger.debug("Medicine found with ID: {}", medicineId);
            Medicine medicine = optional.get();
            ResponseStructure<Medicine> responseStructure = new ResponseStructure<>();
            responseStructure.setStatuscode(HttpStatus.OK.value());
            responseStructure.setMessage("Medicine object found by ID");
            responseStructure.setData(medicine);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ResponseStructure<List<Medicine>>> getAllMedicine() {
        logger.info("Fetching all medicines...");

        List<Medicine> medicines = medicineRepository.findAll();
        if (medicines.isEmpty()) {
            logger.warn("No medicine data found.");
            throw new NoDataFoundInDatabaseException("No Medicine data Found");
        } else {
            logger.debug("Total medicines found: {}", medicines.size());
            ResponseStructure<List<Medicine>> responseStructure = new ResponseStructure<>();
            responseStructure.setStatuscode(HttpStatus.OK.value());
            responseStructure.setMessage("All Medicine objects found");
            responseStructure.setData(medicines);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ResponseStructure<Medicine>> deleteMedicineById(int medicineId) {
        logger.info("Attempting to delete medicine with ID: {}", medicineId);

        Optional<Medicine> optional = medicineRepository.findById(medicineId);
        if (optional.isEmpty()) {
            logger.error("Delete failed: Medicine with ID {} not found", medicineId);
            throw new UserNotFoundException("Medicine Not Found");
        } else {
            Medicine existingMedicine = optional.get();
            medicineRepository.delete(existingMedicine);
            logger.info("Medicine with ID {} deleted successfully", medicineId);

            ResponseStructure<Medicine> responseStructure = new ResponseStructure<>();
            responseStructure.setStatuscode(HttpStatus.OK.value());
            responseStructure.setMessage("Medicine object deleted by ID");
            responseStructure.setData(existingMedicine);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ResponseStructure<Medicine>> updateMedicine(int medicineId, Medicine medicine) {
        logger.info("Updating medicine with ID: {}", medicineId);

        Optional<Medicine> optional = medicineRepository.findById(medicineId);
        if (optional.isEmpty()) {
            logger.warn("Update failed: Medicine not found with ID: {}", medicineId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            Medicine existingMedicine = optional.get();

            if (medicine.getName() != null) existingMedicine.setName(medicine.getName());
            if (medicine.getPrice() != 0.0) existingMedicine.setPrice(medicine.getPrice());
            existingMedicine.setDiscontinued(medicine.isDiscontinued()); // boolean (always update)
            if (medicine.getManufacturerName() != null) existingMedicine.setManufacturerName(medicine.getManufacturerName());
            if (medicine.getType() != null) existingMedicine.setType(medicine.getType());
            if (medicine.getPackSizeLabel() != null) existingMedicine.setPackSizeLabel(medicine.getPackSizeLabel());
            if (medicine.getShortComposition1() != null) existingMedicine.setShortComposition1(medicine.getShortComposition1());
            if (medicine.getShortComposition2() != null) existingMedicine.setShortComposition2(medicine.getShortComposition2());

            Medicine updatedMedicine = medicineRepository.save(existingMedicine);
            logger.info("Medicine updated successfully: {}", updatedMedicine.getMedicineId());

            ResponseStructure<Medicine> responseStructure = new ResponseStructure<>();
            responseStructure.setStatuscode(HttpStatus.OK.value());
            responseStructure.setMessage("Medicine object updated successfully");
            responseStructure.setData(updatedMedicine);

            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ResponseStructure<String>> saveMedicinesFromCsv(MultipartFile file) {
        logger.info("Processing CSV file: {}", file.getOriginalFilename());

        int sentToKafkaCount = 0;
        int duplicateCount = 0;
        int skippedCount = 0;

        try (
            Reader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            CSVReader csv = new CSVReaderBuilder(reader).withSkipLines(1).build() // skip header row
        ) {
            String[] data;
            int rowNum = 1;

            while ((data = csv.readNext()) != null) {
                rowNum++;

                if (data.length < 8) {
                    logger.error("CSV row missing fields (row {}): {} (columns={})",
                            rowNum, Arrays.toString(data), data.length);
                    skippedCount++;
                    continue; 
                }

                String name = data[0].trim();

                if (medicineRepository.findByName(name).isPresent()) {
                    logger.warn("Duplicate medicine skipped: {}", name);
                    duplicateCount++;
                    continue;
                }

                try {
           
                    MedicineEvent event = new MedicineEvent();
                    String priceStr = data[1].trim().replaceAll("[^0-9.]", ""); // clean ₹ or bad chars
                    event.setPrice(priceStr.isEmpty() ? 0.0 : Double.parseDouble(priceStr));
                    event.setDiscontinued(Boolean.parseBoolean(data[2].trim())); // true/false
                    event.setManufacturerName(data[3].trim());
                    event.setType(data[4].trim());
                    event.setPackSizeLabel(data[5].trim());
                    event.setShortComposition1(data[6].trim());
                    event.setShortComposition2(data[7].trim());
                    event.setName(name);

                    medicineProducer.sendMedicine(event);
                    logger.info("Sent medicine to Kafka: {}", name);
                    sentToKafkaCount++;

                } catch (NumberFormatException nfe) {
                    logger.error("Invalid number format in row {}: {}", rowNum, Arrays.toString(data));
                    skippedCount++;
                }
            }
        } catch (IOException e) {
            logger.error("File reading failed: {}", e.getMessage());
            throw new RuntimeException("❌ File reading failed: " + e.getMessage());
        } catch (CsvValidationException e) {
            logger.error("CSV validation error: {}", e.getMessage());
            throw new RuntimeException("❌ CSV validation failed: " + e.getMessage());
        }

        String resultMessage = "✅ Upload complete: " + sentToKafkaCount + " medicine(s) sent to Kafka.";
        if (duplicateCount > 0) resultMessage += " " + duplicateCount + " duplicate(s) skipped.";
        if (skippedCount > 0) resultMessage += " " + skippedCount + " malformed row(s) skipped.";

        ResponseStructure<String> response = new ResponseStructure<>();
        response.setStatuscode(HttpStatus.OK.value());
        response.setMessage("Bulk medicine upload completed via Kafka.");
        response.setData(resultMessage);

        logger.debug("CSV upload summary: {}", resultMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
