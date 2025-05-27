package com.resumeradar.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resumeradar.entity.JobApplication;

@Repository
public interface JobAppRepo extends JpaRepository<JobApplication, String>{

}
