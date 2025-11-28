# Hospital Management Application
The Hospital Management System (HMS) helps manage daily hospital activities for patients, doctors, and admins. Patients can register, book appointments, view prescriptions, and make payments. Doctors and admins can manage schedules, prescriptions, billing, and other basic hospital tasks.
## Table of Contents 
- Features
- Database Schema
- Installation
- Running the Application
## Features
- Doctor and patient registration with OTP-based login.
- Doctors can set availability, view appointments, and create prescriptions.
- Patients can book appointments, view prescriptions, and pay bills online.
- Ability to add medicines and include them inside prescriptions.
- Automatic bill generation with Razorpay payment integration.
- Payment status is updated and tracked after successful transactions.
- Admin can manage doctors, patients, appointments, and billing records.
## Database Schema
Tables:
- users
- patients
- doctor
- appointments
- doctor_availability
- prescription
- medicine_entry
- medicines
- bills
- payment
- otp_verification
## Installation
1. Clone the repository
   
``` bash
      https://github.com/Vaishnavi619/HospitalApplication.git

2.Open MySQL and create a database
 CREATE DATABASE hospitaldb;
