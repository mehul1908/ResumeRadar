package com.resumeradar.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resumes")
@Data
@NoArgsConstructor
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String resumeId;

    @Lob
    private String extractedText;

    @Lob
    private String skills;

    @UpdateTimestamp
    private LocalDateTime uploadedAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

	public Resume(String extractedText, String skills, User user) {
		super();
		this.extractedText = extractedText;
		this.skills = skills;
		this.user = user;
	}
    
    
}

