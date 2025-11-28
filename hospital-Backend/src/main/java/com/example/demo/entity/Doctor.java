package com.example.demo.entity;

import java.time.LocalTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="doctor")
public class Doctor {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int doctorId;
	
	 @Column(nullable = false)
	 private String doctorName;
	 
	 @Column(nullable=false)
	 private int experience;
	 

	 @Column(nullable=true)
	 private LocalTime timings;
	 
	 @Column(nullable=false)	
	 private  String specialization;
	 @Column(nullable=false)
	 private String email;
	 @OneToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "user_id")
	    private User user;
	public Doctor(String doctorName, int experience, LocalTime timings, String specialization, String email,
			User user) {
		super();
		this.doctorName = doctorName;
		this.experience = experience;
		this.timings = timings;
		this.specialization = specialization;
		this.email = email;
		this.user = user;
	}
	public Doctor() {
		super();
	}
	public int getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
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
	public LocalTime getTimings() {
		return timings;
	}
	public void setTimings(LocalTime timings) {
		this.timings = timings;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

		

	
	
	 
}
