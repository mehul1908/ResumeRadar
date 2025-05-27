package com.resumeradar.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "job_matches")
@Data
@NoArgsConstructor
public class JobMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String matchId;

    @ManyToOne
    @JoinColumn(name = "seeker_id")
    private User seeker;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    private BigDecimal matchScore;

    @CreationTimestamp
    private LocalDateTime matchedOn;

	public JobMatch(User seeker, Job job, BigDecimal matchScore) {
		super();
		this.seeker = seeker;
		this.job = job;
		this.matchScore = matchScore;
	}
}

