package com.example.demo.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.exception.NoDataFoundInDatabaseException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.utility.ResponseStructure;
@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository userRepository;
	
	 @Autowired
	    private PasswordEncoder passwordEncoder;
	 @Override
	 public ResponseEntity<ResponseStructure<User>> saveUser(User user) {
	  
	     user.setPassword(passwordEncoder.encode(user.getPassword()));

	     User user1 = userRepository.save(user);

	     ResponseStructure<User> responseStructure = new ResponseStructure<>();
	     responseStructure.setStatuscode(HttpStatus.CREATED.value());
	     responseStructure.setMessage("User Added Successfully");
	     responseStructure.setData(user1); 

	     return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
	 }




	@Override
	public ResponseEntity<ResponseStructure<List<User>>> getAllUsers() {
		List<User> users=userRepository.findAll();
		if(users.isEmpty()) {
			throw new NoDataFoundInDatabaseException("No data Found");
		}else {
			ResponseStructure<List<User>> responseStructure= new ResponseStructure<>();
			responseStructure.setStatuscode(HttpStatus.FOUND.value());
			responseStructure.setMessage("All User objects found");
			responseStructure.setData(users);
			return new ResponseEntity<ResponseStructure<List<User>>>(responseStructure, HttpStatus.FOUND);
		}

	}

	@Override
	public ResponseEntity<ResponseStructure<User>> getUserById(int userId) {
		Optional<User> optional=userRepository.findById(userId);
		if(optional.isEmpty()) {
			throw new UserNotFoundException("User Not Found");
		}else {
			User user=optional.get();
			ResponseStructure<User> responseStructure= new ResponseStructure<User>();
			responseStructure.setStatuscode(HttpStatus.FOUND.value());
			responseStructure.setMessage("User object found by Id");
			responseStructure.setData(user);
			return new ResponseEntity<ResponseStructure<User>>(responseStructure, HttpStatus.FOUND);
		}
	}
	@Override
	public ResponseEntity<ResponseStructure<User>> deleteUser(int  userId) {
		Optional<User>	optional=userRepository.findById(userId);
		if(optional.isEmpty()) {
			throw new UserNotFoundException("User Not Found");
		}else {
			User existingUser=optional.get();
			userRepository.delete(existingUser);
			ResponseStructure<User> responseStructure= new ResponseStructure<User>();
			responseStructure.setStatuscode(HttpStatus.OK.value());
			responseStructure.setMessage("User object deleted by Id");
			responseStructure.setData(existingUser);
			return new ResponseEntity<ResponseStructure<User>>(responseStructure, HttpStatus.OK);
		}

	}



	@Override
	public ResponseEntity<ResponseStructure<User>> updateUser(int userId,User updatedUser) {
		Optional<User> optional=userRepository.findById(userId);
		if(optional.isEmpty()) {
			return null;
		}else {
			User existingUser=optional.get();
			updatedUser.setUserId(existingUser.getUserId());
			ResponseStructure<User> responseStructure= new ResponseStructure<>();
			responseStructure.setStatuscode(HttpStatus.OK.value());
			responseStructure.setMessage("Actor object updated by Id");
			responseStructure.setData(updatedUser);
			userRepository.save(updatedUser);
			return new ResponseEntity<ResponseStructure<User>>(responseStructure, HttpStatus.OK);
			
		}
		
	}
	

}
