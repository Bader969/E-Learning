# LearnSphere - Learning Management System

LearnSphere is a Java-based Learning Management System (LMS) designed for efficient management of educational resources. It supports user roles for teachers and students, along with features like course management, category hierarchies, progress tracking, and certifications.

## Features

- **User Management**: Role-based access for teachers and students.
- **Course Management**: Create, categorize, and manage courses.
- **Category Hierarchies**: Recursive parent-child categories for organizing courses.
- **Progress Tracking**: Track student progress and completion.
- **Ratings and Reviews**: Students can review and rate courses.
- **Purchases and Orders**: Manage course enrollments and transactions.
- **Certifications**: Issue certificates upon course completion.

## Technology Stack

- **Programming Language**: Java
- **Database**: Oracle Database
- **Tools**: SQL, JDBC
- **Architecture**: MVC (Model-View-Controller)

## Database Schema Overview

- **Users (Benutzer)**: Manages teachers and students.
- **Categories (Kategorie)**: Supports recursive category relationships.
- **Courses (Kurs)**: Stores course details linked to teachers and categories.
- **Enrollments (Kurs_Anmeldung)**: Tracks student progress and enrollments.
- **Materials (Lernmaterial)**: Stores course resources.
- **Ratings (Bewertung)**: Captures course ratings and reviews.
- **Orders (Bestellung)**: Manages purchases of courses.
- **Certificates (Zertifikat)**: Stores issued certificates for completed courses.

## Installation

1. **Database Setup**:
   - Run the provided SQL script to set up the database schema.
   - Update database connection settings in `DatabaseManager`.

2. **Project Setup**:
   - Import the project into your preferred Java IDE.
   - Ensure Oracle Database and JDBC drivers are configured.

3. **Run Application**:
   - Execute the `MainApp` class to start the console-based application.

## Usage

- **Teachers**:
  - Create and manage courses.
  - Monitor progress, ratings, and enrollments.

- **Students**:
  - Enroll in courses.
  - Track progress and receive certifications.

## Contributing

Contributions are welcome! Please fork the repository, make your changes, and submit a pull request.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

---

Feel free to reach out for questions or feedback!

