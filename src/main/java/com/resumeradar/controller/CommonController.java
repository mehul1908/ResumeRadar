package com.resumeradar.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumeradar.entity.User;
import com.resumeradar.model.ApiResponse;
import com.resumeradar.model.PasswordUpdateRequest;
import com.resumeradar.model.UpdateUserModel;
import com.resumeradar.service.UserService;
import com.resumeradar.utils.EmailMessage;
import com.resumeradar.utils.RandomPasswordGenerator;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class CommonController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passEncoder;
	
	@Autowired
	private EmailMessage emailMessage;
	
	@GetMapping("/get/{userId}")
	public ResponseEntity<ApiResponse> getUserById(@PathVariable String userId){
		Optional<User> op = userService.getUserById(userId);	
		if(op.isPresent()) {
			return ResponseEntity.ok(new ApiResponse(true, op.get(), "User Found"));
		}
		return ResponseEntity.ok(new ApiResponse(false, op.get(), "User not found"));
	}
	
	@PostMapping("/updatepassword")
	public ResponseEntity<ApiResponse> updatePassword( @Valid @RequestBody PasswordUpdateRequest model ) throws MessagingException{
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof User user) {
			if(passEncoder.matches(model.getOldPassword(), user.getPassword())) {
				String encodedPass = passEncoder.encode(model.getNewPassword());
				User userUpdated = userService.updatePassword(user , encodedPass);
				if(user!=null) {
					return ResponseEntity.ok(new ApiResponse(true , userUpdated , "Password Updated"));
				}
			}else {
				return ResponseEntity.ok(new ApiResponse(false , null , "Password doesn't match"));
			}
		}
		return ResponseEntity.ok(new ApiResponse(false , null , "Password Cann't beUpdated"));
		
	}
	
	@GetMapping("/deactivate")
	public ResponseEntity<ApiResponse>  deactivateYourself(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof User user) {
			User updatedUser = userService.deactivate(user);
			return ResponseEntity.ok(new ApiResponse(true , updatedUser , "User deactivated"));
		}
		else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, null, "Login Again"));
		}
	}
	
	@PostMapping("/updateUser")
	public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserModel model) {
		User user = userService.updateUser(model);
		if(user!=null) {
			return ResponseEntity.ok(new ApiResponse(true, user, "User is updated successfully!!"));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, null, "User is updated!!"));
		
	}
	
	@GetMapping("/forgetpassword")
	public ResponseEntity<ApiResponse> forgetPassword(){
		String rawPassword = RandomPasswordGenerator.generateStrongPassword();
		String randomPassword = passEncoder.encode(rawPassword);
		User user = userService.forgetPassword(randomPassword);
		if(user!=null) {
			try {
				emailMessage.sendPasswordResetEmail(user.getEmail(), user.getName(), rawPassword);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, user, "User not authorized."));
	}
}
