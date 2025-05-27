package com.resumeradar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resumeradar.entity.EmailNotification;

public interface EmailNotificationRepo extends JpaRepository<EmailNotification, String> {

}
