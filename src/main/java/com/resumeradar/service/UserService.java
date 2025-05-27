package com.resumeradar.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.resumeradar.entity.User;
import com.resumeradar.model.LoginModel;
import com.resumeradar.model.RegisterModel;
import com.resumeradar.repo.UserRepo;

import jakarta.validation.Valid;


@Service
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepo uRepo;
	
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
			User user = new User(model.getName(), model.getEmailId(), model.getPassword(), model.getRole());
			uRepo.save(user);
			return user;
		}catch(Exception e) {
			return null;
		}
	}

	

}
