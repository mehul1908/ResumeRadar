package com.resumeradar.entity;

import java.time.LocalDateTime;

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
@Table(name = "email_notifications")
@Data
@NoArgsConstructor
public class EmailNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String subject;

    @Lob
    private String message;

    private LocalDateTime sentAt;

	public EmailNotification(User user, String subject, String message) {
		super();
		this.user = user;
		this.subject = subject;
		this.message = message;
	}
}

