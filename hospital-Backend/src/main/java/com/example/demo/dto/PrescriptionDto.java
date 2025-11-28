
package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;

public class PrescriptionDto {
    private String diagnosis;
    private LocalDate date;
    private List<MedicineLineDto> medicines;
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public List<MedicineLineDto> getMedicines() {
		return medicines;
	}
	public void setMedicines(List<MedicineLineDto> medicines) {
		this.medicines = medicines;
	}

   
}
