package com.resumeradar.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterModel {
	
	@NotBlank(message = "Email id cann't be blank")
	private String emailId;
	
	@NotBlank(message = "Password Cann't be blank")
	@Pattern(
		    regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
		    message = "Password must be at least 8 characters and include a digit, lowercase, uppercase, and special character"
		)
	private String password;
	
	@NotBlank(message = "Role cann't be blank")
	private String role;
	
	@NotBlank(message = "Name cann't be blank")
	private String name;
	
}
