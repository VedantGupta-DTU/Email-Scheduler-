# Email Scheduler Application

A Spring Boot application that allows you to schedule emails to be sent at specific times using Quartz Scheduler with MySQL persistence.

## Features

- 📧 Schedule emails to be sent at specific dates and times
- 🔄 Persistent job storage using MySQL database
- ⏰ Quartz Scheduler for reliable job execution
- 🚀 RESTful API for easy integration
- ✅ Input validation with proper error handling
- 📱 JSON-based API responses

## Prerequisites

- Java 21
- MySQL 8.0+
- Maven 3.6+
- SMTP email service (Gmail, Outlook, etc.)

## Setup

### 1. Database Setup

1. Create a MySQL database named `quartznet`
2. Run the Quartz MySQL schema script to create required tables:
   ```sql
   -- Download and run the official Quartz MySQL script
   -- Available at: https://github.com/quartz-scheduler/quartz/tree/main/quartz-core/src/main/resources/org/quartz/impl/jdbcjobstore
   ```

### 2. Configuration

Update `src/main/resources/application.properties` with your settings:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/quartznet?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=your_username
spring.datasource.password=your_password

# Quartz Configuration
spring.quartz.job-store-type=jdbc
spring.quartz.properties.org.quartz.threadPool.threadCount=5

# Email Configuration (Gmail Example)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**Note:** For Gmail, you'll need to use an App Password instead of your regular password.

### 3. Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

### Schedule Email

**Endpoint:** `POST /api/email/schedule`

**Request Body:**
```json
{
  "to": "recipient@example.com",
  "subject": "Test Email",
  "body": "This is a scheduled email.",
  "scheduledTime": "2024-07-10T15:30:00"
}
```

**Date Format:** `yyyy-MM-dd'T'HH:mm:ss` (24-hour format, no timezone)

**Response Examples:**

Success (200 OK):
```json
{
  "message": "Email scheduled successfully"
}
```

Error - Past Time (400 Bad Request):
```json
{
  "error": "Invalid scheduled time: must be in the future"
}
```

Error - Invalid Format (400 Bad Request):
```json
{
  "error": "Invalid date format. Use yyyy-MM-dd'T'HH:mm:ss"
}
```

## Usage Examples

### Using curl

```bash
curl -X POST http://localhost:8080/api/email/schedule \
  -H "Content-Type: application/json" \
  -d '{
    "to": "test@example.com",
    "subject": "Hello from Email Scheduler",
    "body": "This email was scheduled using the Email Scheduler application!",
    "scheduledTime": "2024-07-10T15:30:00"
  }'
```

### Using Postman

1. Create a new POST request
2. Set URL to: `http://localhost:8080/api/email/schedule`
3. Set Headers: `Content-Type: application/json`
4. Set Body (raw JSON):
```json
{
  "to": "recipient@example.com",
  "subject": "Test Email",
  "body": "This is a scheduled email.",
  "scheduledTime": "2024-07-10T15:30:00"
}
```

## Project Structure

```
src/main/java/com/EmailSender/EmailSender/
├── EmailSenderApplication.java      # Main application class
├── EmailJob.java                    # Quartz job for sending emails
├── EmailSchedulerService.java       # Service for scheduling emails
├── EmailSchedulerController.java    # REST controller
└── EmailScheduleRequest.java        # DTO for API requests
```

## How It Works

1. **Request Reception:** The controller receives a POST request with email details and scheduled time
2. **Validation:** The system validates the input (future time, correct format)
3. **Job Creation:** A Quartz job is created with the email details
4. **Persistence:** The job is stored in MySQL database
5. **Execution:** At the scheduled time, Quartz triggers the job
6. **Email Sending:** The EmailJob sends the email using Spring Mail

## Error Handling

The application provides comprehensive error handling:

- **400 Bad Request:** Invalid input (past time, wrong format)
- **500 Internal Server Error:** Server-side errors (database, email service)

All errors return JSON responses with descriptive messages.

## Technologies Used

- **Spring Boot 3.5.3** - Application framework
- **Quartz Scheduler** - Job scheduling and persistence
- **MySQL** - Database for job storage
- **Spring Mail** - Email sending functionality
- **Maven** - Build tool
- **Java 21** - Programming language

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is open source and available under the [MIT License](LICENSE).

## Support

If you encounter any issues or have questions, please create an issue in the GitHub repository. 