package com.EmailSender.EmailSender;

import lombok.Data;

@Data
public class EmailScheduleRequest {
    private String to;
    private String subject;
    private String body;
    private String scheduledTime; // ISO 8601 string
} 