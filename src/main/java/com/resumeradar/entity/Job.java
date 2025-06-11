package com.resumeradar.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String jobId;

    private String title;

    @Lob 
    private String description;

    @Lob
    private String requiredSkills;

    private String location;

    private String salaryRange;

    @CreationTimestamp
    private LocalDateTime postedAt;

    private Boolean isActive = true;

    private String company;
    
    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    @JsonIgnore
    private User recruiter;

	public Job(String title, String description, String requiredSkills, String location, String salaryRange,
			User recruiter , String company) {
		super();
		this.title = title;
		this.description = description;
		this.requiredSkills = requiredSkills;
		this.location = location;
		this.salaryRange = salaryRange;
		this.recruiter = recruiter;
		this.company = company;
	}
}

