package com.resumeradar.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse 
{
	private String name;
	private String role;
	private String token;
}
