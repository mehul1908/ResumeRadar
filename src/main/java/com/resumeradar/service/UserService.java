package com.resumeradar.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.resumeradar.entity.Role;
import com.resumeradar.entity.User;
import com.resumeradar.exception.UnauthorizedUserException;
import com.resumeradar.model.LoginModel;
import com.resumeradar.model.RegisterModel;
import com.resumeradar.model.UpdateUserModel;
import com.resumeradar.repo.UserRepo;
import com.resumeradar.utils.EmailMessage;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@Service
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepo uRepo;
	
	@Value("${spring.mail.username}")
	private String sender;
	
	@Autowired
	private EmailMessage emailMessage;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> op = uRepo.findById(username);
		if(op.isPresent()) return op.get();
		throw new UsernameNotFoundException("User not found");
	}

	public Optional<User> getUserById(String userid) {
		return uRepo.findById(userid);
	}

	public User login(LoginModel model) {
			Optional<User> op = uRepo.findByEmailAndIsActive(model.getEmailId() , true);
			if(op.isPresent()) return op.get();
			throw new EntityNotFoundException("User Not Found");
	}

	@Transactional
	public User saveUser(@Valid RegisterModel model) throws MessagingException {
			User user = new User(model.getName(), model.getEmailId(), model.getPassword(),
					model.getRole() , model.getEducation() , model.getPhone());
			uRepo.save(user);
			emailMessage.sendRegistrationEmail(user.getEmail(), user.getName());
			return user;
	}

	@PreAuthorize("hasRole('ADMIN')")
	public List<User> getUserByRole(String role) {
		return uRepo.findByRole(Role.valueOf(role));
	}

	@Transactional
	public User updatePassword(User user, String encodedPass) throws MessagingException {
			user.setPassword(encodedPass);
			uRepo.save(user);
			emailMessage.sendPasswordUpdateConfirmation(user.getEmail(), user.getName());
			return user;
		
	}

	@Transactional
	public User deactivate(User user) {
		user.setIsActive(false);
		uRepo.save(user);
		return user;
	}

	@Transactional
	public User updateUser(UpdateUserModel model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof User user) {
			if(model.getEducation()!=null)
				user.setEducation(model.getEducation());
			if(model.getName()!=null)
				user.setName(model.getName());
			if(model.getPhone()!=null)
				user.setPhone(model.getPhone());
			uRepo.save(user);
			return user;
		}
		else {
			throw new UnauthorizedUserException("User is unauthentical or not valid");
		}
	}

	@Transactional
	public User activate(User user) {
		user.setIsActive(true);
		uRepo.save(user);
		return user;
	}

	public List<User> findByIsActive(boolean b) {
		return uRepo.findByIsActive(b);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public List<User> findByIsActiveAndRole(boolean b, Role role) {
		return uRepo.findByIsActiveAndRole(b , role);
	}

	@Transactional
	public User forgetPassword(String randomPassword) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof User user) {
			user.setPassword(randomPassword);
			uRepo.save(user);
			
			return user;
		}else {
			throw new UnauthorizedUserException("User is unauthentical or not valid");
		}
	}

	


	

}
