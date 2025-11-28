package com.example.demo.entity;
import java.time.LocalDate;
import java.util.List;
 
import com.fasterxml.jackson.annotation.JsonIgnore;
 
import jakarta.persistence.*;
 
@Entity
@Table(name = "patients")
public class Patient {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int patientId;
 
    @Column(nullable = false)
    private String fullName;
 
    @Column(nullable = false)
    private int age;
 
    @Column(nullable = false)
    private String gender;
 
    @Column(nullable = false,unique = true)
    private long phone;
 
    @Column(nullable = false)
    private String address;
 
    @Column(nullable = false)
    private LocalDate registeredDate;
    
    @Column(nullable = true)
    private String email;

 
    @ManyToOne
    @JoinColumn(name = "doctor_id") 
    private Doctor doctor;
 
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Appointment> appointments;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;
    
  

	public Patient() {
		super();
	}



	public Patient(String fullName, int age, String gender, long phone, String address, LocalDate registeredDate,
			String email, Doctor doctor, List<Appointment> appointments, User user) {
		super();
		this.fullName = fullName;
		this.age = age;
		this.gender = gender;
		this.phone = phone;
		this.address = address;
		this.registeredDate = registeredDate;
		this.email = email;
		this.doctor = doctor;
		this.appointments = appointments;
		this.user = user;
	}




	public int getPatientId() {
		return patientId;
	}



	public void setPatientId(int patientId) {
		this.patientId = patientId;
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



	public Doctor getDoctor() {
		return doctor;
	}



	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}



	public List<Appointment> getAppointments() {
		return appointments;
	}



	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}



	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}

	
   
 
}