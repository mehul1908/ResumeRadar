package com.resumeradar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
}
