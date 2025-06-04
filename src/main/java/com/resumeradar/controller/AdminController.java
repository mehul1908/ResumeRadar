package com.resumeradar.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumeradar.entity.User;
import com.resumeradar.model.ApiResponse;
import com.resumeradar.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserService userService;
	
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
}
