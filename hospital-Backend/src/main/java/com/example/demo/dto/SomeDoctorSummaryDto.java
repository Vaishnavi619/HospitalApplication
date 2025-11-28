package com.example.demo.dto;

import java.util.List;

public class SomeDoctorSummaryDto {

    private int doctorId;
    private String doctorName;
    private String specialization;

    // Today's available slots (formatted as strings "09:30 AM")
    private List<AvailableSlotDto> todayAvailableSlots;

	public SomeDoctorSummaryDto(int doctorId, String doctorName, String specialization,
			List<AvailableSlotDto> todayAvailableSlots) {
		super();
		this.doctorId = doctorId;
		this.doctorName = doctorName;
		this.specialization = specialization;
		this.todayAvailableSlots = todayAvailableSlots;
	}

	public SomeDoctorSummaryDto() {
		super();
	}

	public final int getDoctorId() {
		return doctorId;
	}

	public final void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public final String getDoctorName() {
		return doctorName;
	}

	public final void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public final String getSpecialization() {
		return specialization;
	}

	public final void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public final List<AvailableSlotDto> getTodayAvailableSlots() {
		return todayAvailableSlots;
	}

	public final void setTodayAvailableSlots(List<AvailableSlotDto> todayAvailableSlots) {
		this.todayAvailableSlots = todayAvailableSlots;
	}

   
}
