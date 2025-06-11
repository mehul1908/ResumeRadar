package com.resumeradar.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumeradar.entity.User;
import com.resumeradar.model.ApiResponse;
import com.resumeradar.model.RegisterModel;
import com.resumeradar.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passEncoder;
	
	@GetMapping("/get/role/{role}")
	public ResponseEntity<ApiResponse> getUserByRole(@PathVariable String role){
		List<User> users = userService.getUserByRole(role);
		if (users==null) {
			return ResponseEntity.ok(new ApiResponse(false, null, "Role is not found"));
		}
		return ResponseEntity.ok(new ApiResponse(true, users, "Data Send"));
	}
	
	@GetMapping("/deactivate/{id}")
	public ResponseEntity<ApiResponse>  deactivate(@PathVariable String id){
		Optional<User> op = userService.getUserById(id);
		if (op.isPresent()) {
			User updatedUser = userService.deactivate(op.get());
			return ResponseEntity.ok(new ApiResponse(true , updatedUser , "User deactivated"));
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new ApiResponse(false, null, "User not found"));
		}
	}
	
	@GetMapping("/activate/{id}")
	public ResponseEntity<ApiResponse> activateUser(@PathVariable String id) {
		Optional<User> op = userService.getUserById(id);
		if (op.isPresent()) {
			User updatedUser = userService.activate(op.get());
			return ResponseEntity.ok(new ApiResponse(true , updatedUser , "User activated"));
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new ApiResponse(false, null, "User not found"));
		}
	}
	
	@PostMapping("/add/admin")
	public ResponseEntity<ApiResponse> addAdmin(@Valid @RequestBody RegisterModel model) throws MessagingException{
		model.setPassword(passEncoder.encode(model.getPassword()));
		User user = userService.saveUser(model);
		if(user!=null) {
			return ResponseEntity.ok(new ApiResponse(true, user, "User Added Successfully"));
		}
		else {
			return ResponseEntity.ok(new ApiResponse(false , null , "User is not able to add"));
		}
	}
	
}
