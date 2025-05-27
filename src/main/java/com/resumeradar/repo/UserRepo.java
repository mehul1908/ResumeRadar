package com.resumeradar.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resumeradar.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, String>{

	Optional<User> findByEmailAndIsActive(String emailId, boolean b);

}
