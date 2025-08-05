package com.EmailSender.EmailSender;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailJob implements Job {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String to = context.getMergedJobDataMap().getString("to");
        String subject = context.getMergedJobDataMap().getString("subject");
        String body = context.getMergedJobDataMap().getString("body");
        String jobId = context.getMergedJobDataMap().getString("jobId");

        // Log the execution for debugging
        System.out.println("Executing email job: " + jobId + " for: " + to);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            
            System.out.println("Email sent successfully for job: " + jobId);
        } catch (Exception e) {
            System.err.println("Failed to send email for job: " + jobId + " - " + e.getMessage());
            throw new JobExecutionException(e);
        }
    }
} 