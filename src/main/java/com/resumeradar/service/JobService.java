package com.resumeradar.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.resumeradar.entity.ApplicationStatus;
import com.resumeradar.entity.Job;
import com.resumeradar.entity.JobApplication;
import com.resumeradar.entity.JobMatch;
import com.resumeradar.entity.Role;
import com.resumeradar.entity.User;
import com.resumeradar.model.JobRegModel;
import com.resumeradar.repo.JobAppRepo;
import com.resumeradar.repo.JobMatchRepo;
import com.resumeradar.repo.JobRepo;

@Service
public class JobService {

	@Autowired
	private JobRepo jobRepo;
	
	@Autowired
	private JobAppRepo jobAppRepo;
	
	@Autowired
	private JobMatchRepo jobMatchRepo;
	
	@Autowired
	private UserService userService;
	
	public Job addJob(JobRegModel model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof User user) {
			Job job = new Job(model.getTitle(), model.getDescription(), model.getSkill(), model.getLocation(), model.getSalaryRange(), user);
			jobRepo.save(job);
			match(job);
			return job;
		}
		return null;
	}


	public Job getJobById(String jobId) {
		Optional<Job> job = jobRepo.findById(jobId);
		if(job.isPresent())
			return job.get();
		return null;
	}
	
	public JobApplication applyJob(String jobId) {
		try {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof User user) {
			Job job = getJobById(jobId);
			if(job!=null) {
				JobApplication jobApp = new JobApplication(job, user, user.getResumes());
				jobAppRepo.save(jobApp);
				return jobApp;
			}
		}
		return null;
		}catch(Exception e) {
			throw e;
		}
	}

	public void match(User user) {
		List<Job> jobs = jobRepo.findByIsActive(true);
		for(Job job : jobs) {
			matchJobAndUser(job , user);
		}
		
	}
	
	private void match(Job job) {
		List<User> users = userService.findByIsActiveAndRole(true, Role.ROLE_JOB_SEEKER);
		for(User user : users) {
			matchJobAndUser(job , user);
		}
		
	}

	private void matchJobAndUser(Job job, User user) {
		
		Set<String> requiredSkillSet = Arrays.stream(job.getRequiredSkills().split(","))
                .map(String::trim)  // remove extra spaces
                .collect(Collectors.toSet());
		
		Set<String> haveSkillSet = Arrays.stream(user.getResumes().getSkills().split(","))
                .map(String::trim)  // remove extra spaces
                .collect(Collectors.toSet());
		
		haveSkillSet.retainAll(requiredSkillSet);
		
		double matchScore = (double) haveSkillSet.size() / requiredSkillSet.size();
		
		JobMatch jobMatch = jobMatchRepo.findBySeekerAndJob(user, job);

		if (jobMatch != null) {
		    if (matchScore < 0.6) {
		        jobMatchRepo.delete(jobMatch);
		    } else {
		        jobMatch.setMatchScore(matchScore);
		        jobMatchRepo.save(jobMatch);
		    }
		} else if (matchScore >= 0.6) {
		    jobMatchRepo.save(new JobMatch(user, job, matchScore));
		}

		
	}


	public List<Job> getJobByMatch() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof User user) {
			List<JobMatch> matches = jobMatchRepo.findBySeeker(user);
			return matches.stream()
	                  .map(JobMatch::getJob)
	                  .collect(Collectors.toList());
		}
		return null;
	}


	public List<Job> getAllJob() {
		return jobRepo.findAll();
	}


	public JobApplication getJobAppById(String applicantId) {
		Optional<JobApplication> op = jobAppRepo.findByApplicationId(applicantId);
		if(op.isPresent()) {
			return op.get();
		}
		return null;
	}


	public void updateJobApp(JobApplication jobApp, ApplicationStatus appStatus) {
		try {
			jobApp.setStatus(appStatus);
			jobAppRepo.save(jobApp);
		}catch(Exception ex){
			throw ex;
		}
		
	}


	public List<User> getCandidateByJobApp(Job job) {
		List<JobApplication> apps = jobAppRepo.findByJob(job);
		return apps.stream()
                  .map(JobApplication::getSeeker)
                  .collect(Collectors.toList());
	}
	

}
