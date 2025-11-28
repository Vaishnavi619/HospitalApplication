package com.example.demo.dto;

import java.time.LocalTime;

public class DoctorDto {
	 

	    private String doctorName;
	    private int experience;
	    private String specialization;
	    private LocalTime timings;
	    private String email;
		public DoctorDto(String doctorName, int experience, String specialization, LocalTime timings, String email) {
			super();
			this.doctorName = doctorName;
			this.experience = experience;
			this.specialization = specialization;
			this.timings = timings;
			this.email = email;
		}
		public DoctorDto() {
			super();
		}
		public String getDoctorName() {
			return doctorName;
		}
		public void setDoctorName(String doctorName) {
			this.doctorName = doctorName;
		}
		public int getExperience() {
			return experience;
		}
		public void setExperience(int experience) {
			this.experience = experience;
		}
		public String getSpecialization() {
			return specialization;
		}
		public void setSpecialization(String specialization) {
			this.specialization = specialization;
		}
		public LocalTime getTimings() {
			return timings;
		}
		public void setTimings(LocalTime timings) {
			this.timings = timings;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		
	    
}
