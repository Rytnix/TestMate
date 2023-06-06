# Testmate Exam Portal App

Testmate is a web-based exam portal application developed using Java Spring Boot framework. It provides a platform for conducting online exams, managing question banks, and generating results. This README file provides an overview of the Testmate app and explains how to set it up and use it.

![image](https://drive.google.com/uc?export=view&id=1yywPf4mr_yGoLTQJnWSq7galvZIupula)

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Features

- User authentication and authorization
- Admin panel for managing exams, question banks, and users
- Creating and scheduling exams
- Adding, editing, and deleting questions
- Randomized question sets for each exam
- Timer for exams
- Real-time notifications for upcoming exams
- Automatic grading of exams
- Viewing and exporting exam results

## Prerequisites

Make sure you have the following prerequisites installed on your system:

- Java Development Kit (JDK) 11 or higher
- Maven (for building the project)
- MySQL or any other relational database management system

## Installation

1. Clone the repository to your local machine:
```bash
   git clone https://github.com/Rytnix/TestMate.git
```
2. Run
```bash
    mvn clean install
```
3. Run
```bash
   java -jar target/Testmate-<version>.jar
```
4. Create Databse `ExamPortal`
```sql
   CREATE DATABASE ExamPortal
```
5. The Testmate app should now be running on http://localhost:9000. Open this URL in your web browser to access the application.

## Usage

1. Open the Testmate app in your web browser.

2. Sign up as a new user or log in with your existing credentials.

3. If you have admin privileges, you can access the admin panel to manage exams, question banks, and users.

4. Create exams and add questions to the question bank.

5. Schedule exams and set their duration.

6. Students can log in and take exams within the specified time frame.

7. Exam results can be viewed and exported in various formats.

For more detailed information on using the Testmate app, refer to the user documentation or the in-app help section.

## Contributing

Contributions are welcome! If you want to contribute to the Testmate app, please follow these steps:

1. Fork the repository.

2. Create a new branch for your feature or bug fix.

3. Make your changes and test thoroughly.

4. Commit your changes and push them to your fork.

5. Submit a pull request describing your changes.

We appreciate your contributions and will review and merge the changes if they align with the project's goals.

