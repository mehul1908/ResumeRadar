package com.resumeradar.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailMessage {

	@Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    private static String loginUrl = "www.resumeradar.com/login";
    
    private static String supportEmail = "support@resumeradar.com";

    public void sendRegistrationEmail(String toEmail, String name) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("email", toEmail);
        context.setVariable("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        context.setVariable("loginUrl", loginUrl);
        context.setVariable("supportEmail", supportEmail);

        String htmlContent = templateEngine.process("registration-email", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("Welcome to ResumeRadar – Registration Successful!");
        helper.setText(htmlContent, true); // true = isHtml
        helper.setFrom(fromEmail);

        mailSender.send(message);
    }
    
    public void sendPasswordResetEmail(String toEmail, String name, String newPassword) throws MessagingException {
        // Thymeleaf context setup
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("password", newPassword);
        context.setVariable("loginUrl", loginUrl);
        context.setVariable("supportEmail", supportEmail);

        // Process the HTML template
        String htmlContent = templateEngine.process("forget-password-email", context);

        // Construct the message
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("Password Reset - ResumeRadar");
        helper.setText(htmlContent, true);
        helper.setFrom(fromEmail);

        mailSender.send(message);
    }
    
    public void sendPasswordUpdateConfirmation(String toEmail, String name) throws MessagingException {
        // Prepare Thymeleaf context
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        context.setVariable("loginUrl", loginUrl);
        context.setVariable("supportUrl", supportEmail);

        // Generate HTML from template
        String htmlContent = templateEngine.process("password-updated-email", context);

        // Prepare message
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("Your Password Has Been Updated");
        helper.setText(htmlContent, true);
        helper.setFrom("no-reply@resumeradar.com");

        mailSender.send(message);
    }
    
    
    public void sendJobApplicationConfirmation(
            String toEmail,
            String name,
            String jobTitle,
            String companyName
    ) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("jobTitle", jobTitle);
        context.setVariable("companyName", companyName);
        context.setVariable("submittedDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        context.setVariable("supportEmail", supportEmail);

        String htmlContent = templateEngine.process("job-application-success", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("Application Submitted – " + jobTitle + " at " + companyName);
        helper.setText(htmlContent, true);
        helper.setFrom("no-reply@resumeradar.com");

        mailSender.send(message);
    }
    
    public void sendJobInProcessNotification(
            String toEmail,
            String name,
            String jobTitle,
            String companyName
    ) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("jobTitle", jobTitle);
        context.setVariable("companyName", companyName);
        context.setVariable("loginUrl", loginUrl);
        context.setVariable("supportEmail", supportEmail);

        String htmlContent = templateEngine.process("job-in-process-email", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("Your Application is Under Review – " + jobTitle);
        helper.setText(htmlContent, true);
        helper.setFrom("no-reply@resumeradar.com");

        mailSender.send(message);
    }


}
