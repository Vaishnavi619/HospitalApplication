package com.example.demo.utility;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Patient;
import com.opencsv.CSVReader;

public class CSVHelper {

    public static List<Patient> parseCSV(MultipartFile file) {
        List<Patient> patients = new ArrayList<>();

        try (
            InputStream is = file.getInputStream();
            CSVReader reader = new CSVReader(new InputStreamReader(is))
        ) {
            String[] line;
            boolean isFirstRow = true;

            while ((line = reader.readNext()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;                     continue;
                }

                Patient patient = new Patient();
                patient.setFullName(line[0]);                             
                patient.setAge(Integer.parseInt(line[1]));                
                patient.setGender(line[2]);                               
                patient.setPhone(Long.parseLong(line[3]));               
                patient.setAddress(line[4]);                             

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                patient.setRegisteredDate(LocalDate.parse(line[5], formatter)); 

                patient.setEmail(line[6]);                                 

                patients.add(patient);
            }
        } catch (Exception e) {
            e.printStackTrace(); 
        }

        return patients;
    }
}
