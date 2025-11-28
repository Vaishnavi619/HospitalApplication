package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.utility.ResponseStructure;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



	@PostMapping
	public ResponseEntity<ResponseStructure<User>> saveUser(@RequestBody User user) {
		return userService.saveUser(user);
	} 


	@GetMapping("/{userId}")
	public ResponseEntity<ResponseStructure<User>> getUserById(@PathVariable("userId") int  userId){
		return userService.getUserById(userId);
	}

	@GetMapping
	public  ResponseEntity<ResponseStructure<List<User>>> getAllUsers(){
		return userService.getAllUsers();
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<ResponseStructure<User>> deleteUser(@PathVariable("userId")   int userId){
		return userService.deleteUser(userId);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<ResponseStructure<User>> updateUser(@PathVariable("userId") int userId,@RequestBody User updatedUser){
		return userService.updateUser(userId, updatedUser);
	}
}

