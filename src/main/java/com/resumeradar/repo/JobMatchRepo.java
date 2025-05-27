package com.resumeradar.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resumeradar.entity.JobMatch;

@Repository
public interface JobMatchRepo extends JpaRepository<JobMatch, String>{

}
