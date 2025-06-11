package com.resumeradar.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.resumeradar.entity.Role;
import com.resumeradar.entity.User;
import com.resumeradar.model.LoginModel;
import com.resumeradar.model.RegisterModel;
import com.resumeradar.model.UpdateUserModel;
import com.resumeradar.repo.UserRepo;
import com.resumeradar.utils.EmailMessage;

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
		return null;
	}

	public Optional<User> getUserById(String userid) {
		return uRepo.findById(userid);
	}

	public User login(LoginModel model) {
		try {
			Optional<User> op = uRepo.findByEmailAndIsActive(model.getEmailId() , true);
			if(op.isPresent()) return op.get();
			return null;
		}
		catch(Exception e) {
			return null;
		}
	}

	public User saveUser(@Valid RegisterModel model) {
		try {
			User user = new User(model.getName(), model.getEmailId(), model.getPassword(),
					model.getRole() , model.getEducation() , model.getPhone());
			uRepo.save(user);
			emailMessage.sendRegistrationEmail(user.getEmail(), user.getName());
			return user;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<User> getUserByRole(String role) {
		// TODO Auto-generated method stub
		try {
			return uRepo.findByRole(Role.valueOf(role));
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public User updatePassword(User user, String encodedPass) {
		try {
			user.setPassword(encodedPass);
			uRepo.save(user);
			emailMessage.sendPasswordUpdateConfirmation(user.getEmail(), user.getName());
			return user;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public User deactivate(User user) {
		user.setIsActive(false);
		uRepo.save(user);
		return user;
	}

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
		return null;
	}

	public User activate(User user) {
		user.setIsActive(true);
		uRepo.save(user);
		return user;
	}

	public List<User> findByIsActive(boolean b) {
		return uRepo.findByIsActive(b);
	}

	public List<User> findByIsActiveAndRole(boolean b, Role role) {
		return uRepo.findByIsActiveAndRole(b , role);
	}

	public User forgetPassword(String randomPassword) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof User user) {
			user.setPassword(randomPassword);
			uRepo.save(user);
			
			return user;
		}
		return null;
	}

	


	

}
