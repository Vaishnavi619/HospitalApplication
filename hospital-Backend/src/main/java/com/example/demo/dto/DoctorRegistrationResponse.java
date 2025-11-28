package com.example.demo.dto;

import com.example.demo.entity.Doctor;

public class DoctorRegistrationResponse {
    private Doctor doctor;
    private String username;
    private String password;

    public Doctor getDoctor() {
        return doctor;
    }
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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
