package com.EmailSender.EmailSender;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailSchedulerController {

    @Autowired
    private EmailSchedulerService emailSchedulerService;

    @PostMapping("/schedule")
    public ResponseEntity<Map<String, String>> scheduleEmail(@RequestBody EmailScheduleRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            // Parse ISO 8601 date string
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date scheduledTime = sdf.parse(request.getScheduledTime());
            if (scheduledTime.before(new Date())) {
                response.put("error", "Invalid scheduled time: must be in the future");
                return ResponseEntity.badRequest().body(response);
            }
            emailSchedulerService.scheduleEmail(request.getTo(), request.getSubject(), request.getBody(), scheduledTime);
            response.put("message", "Email scheduled successfully");
            return ResponseEntity.ok(response);
        } catch (ParseException e) {
            response.put("error", "Invalid date format. Use yyyy-MM-dd'T'HH:mm:ss");
            return ResponseEntity.badRequest().body(response);
        } catch (org.quartz.SchedulerException e) {
            if (e.getMessage().contains("already scheduled")) {
                response.put("error", "Email already scheduled for this recipient at this time");
                return ResponseEntity.badRequest().body(response);
            }
            response.put("error", "Failed to schedule email: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        } catch (Exception e) {
            response.put("error", "Failed to schedule email: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
} 