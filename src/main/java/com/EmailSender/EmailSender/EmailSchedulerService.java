package com.EmailSender.EmailSender;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmailSchedulerService {

    @Autowired
    private Scheduler scheduler;

    public void scheduleEmail(String to, String subject, String body, Date scheduledTime) throws SchedulerException {
        // Create a unique job key based on content and time to prevent duplicates
        String jobKey = generateJobKey(to, subject, body, scheduledTime);
        
        // Check if job already exists
        if (scheduler.checkExists(JobKey.jobKey(jobKey, "email-jobs"))) {
            throw new SchedulerException("Email job already scheduled for this recipient at this time");
        }
        
        JobDetail jobDetail = JobBuilder.newJob(EmailJob.class)
                .withIdentity(jobKey, "email-jobs")
                .usingJobData("to", to)
                .usingJobData("subject", subject)
                .usingJobData("body", body)
                .usingJobData("jobId", jobKey)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger_" + jobKey, "email-triggers")
                .startAt(scheduledTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
    
    private String generateJobKey(String to, String subject, String body, Date scheduledTime) {
        // Create a deterministic key based on email content and time
        String content = to + "|" + subject + "|" + body + "|" + scheduledTime.getTime();
        return "email_" + content.hashCode();
    }
} 