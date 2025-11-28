package com.example.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;


import com.example.demo.entity.User;

import com.example.demo.utility.ResponseStructure;

public interface UserService {
	public ResponseEntity<ResponseStructure<User>> saveUser(User user);
	public ResponseEntity<ResponseStructure<User>> getUserById(int userId);
	public  ResponseEntity<ResponseStructure<List<User>>> getAllUsers();
	public ResponseEntity<ResponseStructure<User>> deleteUser(int id);
	public ResponseEntity<ResponseStructure<User>> updateUser(int userId,User updateUser);
	
}
