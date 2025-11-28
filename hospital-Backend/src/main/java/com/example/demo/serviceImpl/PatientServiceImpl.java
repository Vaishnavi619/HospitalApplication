package com.example.demo.serviceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.PatientDto;
import com.example.demo.ennums.Role;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.entity.PatientIndex;
import com.example.demo.entity.User;
import com.example.demo.exception.NoDataFoundInDatabaseException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.PatientSearchRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PatientService;
import com.example.demo.utility.CSVHelper;
import com.example.demo.utility.ResponseStructure;
import com.example.demo.utility.UserPatientLinker;

@Service
public class PatientServiceImpl implements PatientService{
	@Autowired
	private PatientRepository patientRepository;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private  UserPatientLinker userPatientLinker;
	@Autowired
	private PatientSearchRepository patientSearchRepository;
	@Autowired
	private KafkaTemplate<String, PatientDto> patientKafkaTemplate;

	private static final String TOPIC = "bulk-patient-topic";
	@Autowired
	private AppointmentRepository appointmentRepository;
	@Override
	public ResponseEntity<ResponseStructure<Patient>> savePatient(PatientDto patientDto) {

	  
	    Patient patient = new Patient();
	    patient.setFullName(patientDto.getFullName());
	    patient.setAge(patientDto.getAge());
	    patient.setGender(patientDto.getGender());
	    patient.setPhone(patientDto.getPhone());
	    patient.setAddress(patientDto.getAddress());
	    patient.setRegisteredDate(patientDto.getRegisteredDate());

	  
	    String cleanedName = patientDto.getFullName().replaceAll("[^a-zA-Z]", "").toLowerCase();
	    String email = (patientDto.getEmail() != null && !patientDto.getEmail().isEmpty())
	            ? patientDto.getEmail()
	            : cleanedName + "@gmail.com";

	
	    patient.setEmail(email);

	    User user = userPatientLinker.generateAndLinkUserForPatient(patient, email);
	    patient.setUser(user);

	    Patient savedPatient = patientRepository.save(patient);

	
	    PatientIndex index = new PatientIndex();
	    index.setId(String.valueOf(savedPatient.getPatientId())); // ✅ Use DB id as ES id
	    index.setFullName(savedPatient.getFullName());
	    index.setAge(savedPatient.getAge());
	    index.setGender(savedPatient.getGender());
	    index.setPhone(savedPatient.getPhone());
	    index.setAddress(savedPatient.getAddress());
	    index.setRegisteredDate(savedPatient.getRegisteredDate().toString()); 
	    index.setEmail(savedPatient.getEmail()); 

	    try {
	        patientSearchRepository.save(index);
	    } catch (Exception e) {
	        System.err.println("⚠️ Failed to index patient in Elasticsearch: " + e.getMessage());
	 
	    }

	
	    ResponseStructure<Patient> response = new ResponseStructure<>();
	    response.setStatuscode(HttpStatus.CREATED.value());
	    response.setMessage("✅ Patient registered successfully and indexed in Elasticsearch");
	    response.setData(savedPatient);

	    return new ResponseEntity<>(response, HttpStatus.CREATED);
	}



	@Override
	public ResponseEntity<ResponseStructure<List<Patient>>> getAllPatients() {
		List<Patient> patients=patientRepository.findAll();
		if(patients.isEmpty()) {
			throw new NoDataFoundInDatabaseException("No data Found");
		}else {
			ResponseStructure<List<Patient>> responseStructure= new ResponseStructure<>();
			responseStructure.setStatuscode(HttpStatus.OK.value());
			responseStructure.setMessage("All Patients objects found");
			responseStructure.setData(patients);
			return new ResponseEntity<ResponseStructure<List<Patient>>>(responseStructure,HttpStatus.OK);
		}
	}
	@Override
	public ResponseEntity<ResponseStructure<Patient>> getPatientById(int patientId) {
		Optional<Patient> optional=patientRepository.findById(patientId);
		if(optional.isEmpty()) {
			throw new UserNotFoundException("Patient Not Found");
		}else {
			Patient patient=optional.get();
			ResponseStructure<Patient> responseStructure= new ResponseStructure<Patient>();
			responseStructure.setStatuscode(HttpStatus.OK.value());
			responseStructure.setMessage("Patient object found by Id");
			responseStructure.setData(patient);
			return new ResponseEntity<ResponseStructure<Patient>>(responseStructure, HttpStatus.OK);
		}
	}
	@Override
	public ResponseEntity<ResponseStructure<Patient>> deletePatientById(int patientId) {
		Optional<Patient>	optional=patientRepository.findById(patientId);
		if(optional.isEmpty()) {
			throw new UserNotFoundException("Patient Not Found");
		}else {
			Patient existingPatient=optional.get();
			patientRepository.delete(existingPatient);
			ResponseStructure<Patient> responseStructure= new ResponseStructure<Patient>();
			responseStructure.setStatuscode(HttpStatus.OK.value());
			responseStructure.setMessage("Patient object deleted by Id");
			responseStructure.setData(existingPatient);
			return new ResponseEntity<ResponseStructure<Patient>>(responseStructure, HttpStatus.OK);
		}
	}
	@Override
	public ResponseEntity<ResponseStructure<Patient>> updatePatient(int patientId, Patient updatedPatient) {
		Optional<Patient> optional = patientRepository.findById(patientId);

		if (optional.isEmpty()) {
		
			ResponseStructure<Patient> responseStructure = new ResponseStructure<>();
			responseStructure.setStatuscode(HttpStatus.OK.value());
			responseStructure.setMessage("Patient not found");
			responseStructure.setData(null);
			return new ResponseEntity<>(responseStructure, HttpStatus.OK);
		} else {
			Patient existingPatient = optional.get();

			existingPatient.setFullName(updatedPatient.getFullName());
			existingPatient.setAge(updatedPatient.getAge());
			existingPatient.setGender(updatedPatient.getGender());
			existingPatient.setPhone(updatedPatient.getPhone());
			existingPatient.setAddress(updatedPatient.getAddress());

			Patient savedPatient = patientRepository.save(existingPatient);


			ResponseStructure<Patient> responseStructure = new ResponseStructure<>();
			responseStructure.setStatuscode(HttpStatus.OK.value());
			responseStructure.setMessage("Patient updated successfully");
			responseStructure.setData(savedPatient);

			return ResponseEntity.ok(responseStructure);
		}
	}
	@Override
	public ResponseEntity<ResponseStructure<Patient>> getLoggedInPatient() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String username = auth.getName();

	    User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	    Patient patient = patientRepository.findByUser(user)
	            .orElseThrow(() -> new RuntimeException("Patient not found for this user"));

	    ResponseStructure<Patient> responseStructure = new ResponseStructure<>();
	    responseStructure.setStatuscode(HttpStatus.OK.value());
	    responseStructure.setMessage("Logged-in patient fetched successfully");
	    responseStructure.setData(patient);

	    return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Appointment>>> getLoggedInPatientAppointments() {
		Patient patient = getLoggedInPatient().getBody().getData(); // ✅


	    List<Appointment> appointments = appointmentRepository.findByPatient(patient);

	    ResponseStructure<List<Appointment>> response = new ResponseStructure<>();
	    response.setStatuscode(HttpStatus.OK.value());
	    response.setMessage("Appointments for logged-in patient");
	    response.setData(appointments);

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> saveBulkPatients(MultipartFile file) {
	    List<Patient> patients = CSVHelper.parseCSV(file);

	    int sentCount = 0;
	    int duplicateCount = 0;

	    for (Patient patient : patients) {
	        Optional<Patient> existing = patientRepository.findByPhone(patient.getPhone());
	        if (existing.isPresent()) {
	            duplicateCount++;
	            continue;
	        }

	        PatientDto dto = new PatientDto();
	        dto.setFullName(patient.getFullName());
	        dto.setAge(patient.getAge());
	        dto.setGender(patient.getGender());
	        dto.setPhone(patient.getPhone());
	        dto.setAddress(patient.getAddress());
	        dto.setRegisteredDate(patient.getRegisteredDate());
	        dto.setEmail(patient.getEmail());

	  
	        patientKafkaTemplate.send(TOPIC, dto);
	        sentCount++;
	    }

	 
	    String message = "📦 Kafka upload: " + sentCount + " patient(s) sent.";
	    if (duplicateCount > 0) {
	        message += " ❗ " + duplicateCount + " duplicate(s) skipped.";
	    }

	    ResponseStructure<String> responseStructure = new ResponseStructure<>();
	    responseStructure.setStatuscode(HttpStatus.OK.value());
	    responseStructure.setMessage("Bulk upload via Kafka");
	    responseStructure.setData(message);

	    return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	private String generateUniqueUsername(String baseUsername) {
	    String username = baseUsername;
	    int count = 0;
	    while (userRepository.findByUsername(username).isPresent()) {
	        count++;
	        username = baseUsername + count;
	    }
	    return username;
	}



}



