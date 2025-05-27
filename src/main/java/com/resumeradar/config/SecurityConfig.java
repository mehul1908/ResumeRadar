package com.resumeradar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig 
{
	@Autowired
	private JWTFilter jwtFilter;
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception 
    {
		http.csrf(csrf->csrf.disable())
	    .authorizeHttpRequests(auth -> 
	        auth.requestMatchers("/auth/**","/swagger-ui/**", 
                    "/v3/api-docs/**", 
                    "/swagger-ui.html",
                    "/webjars/**").permitAll()
	        .requestMatchers("/**").permitAll()
	        
	        .anyRequest().permitAll())
	    .exceptionHandling(ex -> 
	        ex.accessDeniedPage("/auth/wrongauth"));
		
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


