package com.example.demo.dto;

import com.example.demo.entity.Patient;

public class PatientRegistrationResponse {
	
	private Patient patient;
    private String username;
    private String password;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
