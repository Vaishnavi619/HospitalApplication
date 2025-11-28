package com.example.demo.dto;

public class DoctorAvailabilityDTO {
    private String dayOfWeek; 
    private String startTime; 
    private String endTime;   
    private Integer slotDuration; 
	public DoctorAvailabilityDTO(String dayOfWeek, String startTime, String endTime, Integer slotDuration) {
		super();
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
		this.slotDuration = slotDuration;
	}
	public DoctorAvailabilityDTO() {
		super();
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getSlotDuration() {
		return slotDuration;
	}
	public void setSlotDuration(Integer slotDuration) {
		this.slotDuration = slotDuration;
	}
    
}

