package com.resumeradar.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resumeradar.entity.Job;

@Repository
public interface JobRepo extends JpaRepository<Job, String>{

}
