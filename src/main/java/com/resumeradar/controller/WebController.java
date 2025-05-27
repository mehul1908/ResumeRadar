package com.resumeradar.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.resumeradar.config.JWTUtils;
import com.resumeradar.entity.User;
import com.resumeradar.model.ApiResponse;
import com.resumeradar.model.LoginModel;
import com.resumeradar.model.LoginResponse;
import com.resumeradar.model.RegisterModel;
import com.resumeradar.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class WebController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passEncoder;
	
	@Autowired
	private JWTUtils jwtUtils;
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginModel model){
		
		User user =  userService.login(model);
		
		if(user==null) {
			ApiResponse res = new ApiResponse(false, null, "Email Id is Wrong!!");
			return ResponseEntity.ok(res);
		}
		
		boolean flag = passEncoder.matches(model.getPassword(), user.getPassword());
		if(flag) {
			String token = jwtUtils.generateToken(user.getUserId());
			LoginResponse lr = new LoginResponse(user.getName(), user.getRole().name(), token);
			return ResponseEntity.ok(new ApiResponse(true, lr, "User Login Successfully"));
		}
		
		return ResponseEntity.ok(new ApiResponse(false, null, "Email and Password doesn't match"));
	}
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegisterModel model){
		model.setPassword(passEncoder.encode(model.getPassword()));
		User user = userService.saveUser(model);
		if(user!=null) {
			return ResponseEntity.ok(new ApiResponse(true, user, "User Added Successfully"));
		}
		else {
			return ResponseEntity.ok(new ApiResponse(false , null , "User is not able to add"));
		}
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
}
