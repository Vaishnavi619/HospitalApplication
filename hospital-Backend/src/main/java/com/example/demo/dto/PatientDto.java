package com.example.demo.dto;

import java.time.LocalDate;

public class PatientDto {
    private String fullName;
    private int age;
    private String gender;
    private long phone;
    private String address;
    private LocalDate registeredDate;
    private String email;
	public PatientDto(String fullName, int age, String gender, long phone, String address, LocalDate registeredDate,
			String email) {
		super();
		this.fullName = fullName;
		this.age = age;
		this.gender = gender;
		this.phone = phone;
		this.address = address;
		this.registeredDate = registeredDate;
		this.email = email;
	}
	public PatientDto() {
		super();
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public LocalDate getRegisteredDate() {
		return registeredDate;
	}
	public void setRegisteredDate(LocalDate registeredDate) {
		this.registeredDate = registeredDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
    
    
	
	

  
}

