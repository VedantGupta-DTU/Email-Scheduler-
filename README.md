# Email Scheduler Application

A Spring Boot application that allows you to schedule emails to be sent at specific times using Quartz Scheduler with MySQL persistence.  
**Now supports clustered, highly available deployments!**

---

## Features

- 📧 Schedule emails to be sent at specific dates and times
- 🔄 Persistent job storage using MySQL database
- ⏰ Quartz Scheduler for reliable, distributed job execution
- 🚀 RESTful API for easy integration
- ✅ Input validation with proper error handling
- 📱 JSON-based API responses
- 🏢 **Clustered mode:** Run multiple instances for high availability—only one instance will execute each job

---

## Prerequisites

- Java 21+
- MySQL 8.0+
- Maven 3.6+
- SMTP email service (Gmail, Outlook, etc.)

---

## Setup

### 1. Database Setup

1. Create a MySQL database named `quartznet`
2. Run the official Quartz MySQL schema script to create required tables:
   ```sql
   -- Download from https://github.com/quartz-scheduler/quartz/blob/main/quartz-core/src/main/resources/org/quartz/impl/jdbcjobstore/tables_mysql_innodb.sql
   -- Then run in your MySQL client
   ```

### 2. Configure Application

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/quartznet?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD

# Quartz Clustering
spring.quartz.job-store-type=jdbc
spring.quartz.properties.org.quartz.jobStore.isClustered=true
spring.quartz.properties.org.quartz.scheduler.instanceId=AUTO
spring.quartz.properties.org.quartz.scheduler.instanceName=EmailSchedulerCluster
spring.quartz.properties.org.quartz.jobStore.clusterCheckinInterval=20000
spring.quartz.properties.org.quartz.jobStore.misfireThreshold=60000
spring.quartz.jdbc.initialize-schema=never

# Email (SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_EMAIL_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## Running in Clustered Mode

You can run **multiple instances** of this application (on the same or different machines) and Quartz will ensure that **only one instance executes each scheduled job**.

### **Start Multiple Instances (Example on One Machine):**

```sh
java -jar target/EmailSender-0.0.1-SNAPSHOT.jar --server.port=8080
java -jar target/EmailSender-0.0.1-SNAPSHOT.jar --server.port=8081
java -jar target/EmailSender-0.0.1-SNAPSHOT.jar --server.port=8082
```

- All instances connect to the same MySQL database.
- All instances register themselves in the Quartz cluster.
- When a job is due, **only one instance** will execute it, even if all are running.

---

## How Clustering Works

- **Shared Database:** All instances use the same MySQL database for job storage and coordination.
- **Leader Election:** Quartz uses database locks to ensure only one instance picks up and executes a job.
- **Failover:** If one instance goes down, another will take over job execution.
- **No Duplicates:** Even if you POST the same job to multiple instances, only one will execute it.

---

## API Usage

**POST** `/api/email/schedule`

**Request Body:**
```json
{
  "to": "recipient@example.com",
  "subject": "Test Email",
  "body": "This is a scheduled email.",
  "scheduledTime": "2025-08-05T15:30:00"
}
```

**Responses:**
- `{"message": "Email scheduled successfully"}` (HTTP 200)
- `{"error": "Invalid scheduled time: must be in the future"}` (HTTP 400)
- `{"error": "Email already scheduled for this recipient at this time"}` (HTTP 400)
- `{"error": "Invalid date format. Use yyyy-MM-dd'T'HH:mm:ss"}` (HTTP 400)

---

## Project Structure

- `EmailSenderApplication.java` — Main Spring Boot entry point
- `EmailSchedulerController.java` — REST API controller
- `EmailSchedulerService.java` — Business logic for scheduling
- `EmailJob.java` — Quartz job for sending emails
- `EmailScheduleRequest.java` — DTO for API requests
- `application.properties` — All configuration

---

## Production Recommendations

- Use a **robust MySQL server** accessible by all app instances
- Use a **load balancer** to distribute API requests across instances
- Secure your SMTP credentials and database access
- Monitor logs for job execution and failures

---

## Contributing

Pull requests welcome! Please open an issue first to discuss changes.

---

## License

MIT

---

## Author

[Your Name](https://github.com/VedantGupta-DTU)
