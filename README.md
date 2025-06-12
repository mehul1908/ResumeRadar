ResumeRadar
ResumeRadar is a Java-based job portal system built with Spring Boot. It provides functionality for job seekers and recruiters including resume uploads, job postings, application tracking, and JWT-secured authentication (including Google OAuth2 login).

✨ Features
User Roles: Supports Admin, Job Seeker, and Recruiter roles.

Authentication:

Email/Password based login

Google OAuth2 login with auto-registration

JWT token-based session management

Resume Management: Upload, parse, and analyze resumes.

Job Applications: Job seekers can apply to jobs and track their application status.

Admin Management: Admin users can view and manage registered users and jobs.

API Security: Custom JWTFilter, role-based access control, and token blacklisting.

Error Handling: Global exception handling for robust API responses.

Documentation: Swagger integrated for API testing and documentation.

 

src/main/java/com/resumeradar
│
├── config/              # Security, JWT, and OAuth2 configurations
├── controller/          # REST API controllers for auth, jobs, resumes, etc.
├── entity/              # Entity classes: User, Role, Resume, Job, etc.
├── exception/           # Custom and global exception handling
├── model/               # DTOs for API requests and responses
├── repo/                # Spring Data JPA repositories
├── service/             # Business logic services
├── utils/               # Utility classes (e.g., Token blacklist)
└── ResumeRadarApplication.java  # Entry point 


⚙️ Technologies Used
Java 17

Spring Boot 3

Spring Security with JWT & OAuth2

JPA / Hibernate

MySQL / PostgreSQL (or any supported DB)

Jakarta Validation

Swagger (OpenAPI 3)

Lombok

🔐 Security Overview
JWT Token Security: All endpoints (except /auth/**) are protected with JWT.

Google SSO: Supports OAuth2 login, with JWT generation and optional auto-registration.

Token Blacklisting: Tokens are checked for revocation using TokenBlacklistService.

Role-Based Authorization: Admin, Recruiter, and Job Seeker roles enforced at controller level.

🚀 Running the Project
Prerequisites
Java 17

Maven

MySQL or other configured DB

OAuth credentials from Google (if using OAuth login)

Steps
bash
Copy
Edit
# Clone the repository
git clone https://github.com/mehul1908/ResumeRadar.git

# Navigate to project directory
cd ResumeRadar

# Update application.properties with your DB and Google OAuth config

# Build and run the project
./mvnw spring-boot:run


### 🔧 Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/mehul1908/resumeradar.git
   cd resumeradar
