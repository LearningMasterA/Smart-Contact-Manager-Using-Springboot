Smart Contact Manager

A full-stack contact management application built with Spring Boot and Thymeleaf, allowing users to store, organize, and manage contacts efficiently. The application includes secure authentication, CRUD operations, search functionality, and a responsive UI.

Table of Contents

Features

Technologies Used

Demo

Installation

Usage

Project Structure

Future Enhancements

License

Features

User Authentication (Login & Signup)

Add, Edit, Delete, and View Contacts

Search contacts by name, email, or phone

Responsive design using Thymeleaf templates and CSS

Pagination for large contact lists

Toast/alert notifications for actions (add/update/delete)

Backend validation for secure and consistent data

Technologies Used

Backend:

Java 17+

Spring Boot

Spring Security (Authentication & Authorization)

Spring Data JPA

MySQL or H2 Database

REST APIs (for potential API integrations)

Frontend:

Thymeleaf (Server-side templating)

HTML, CSS, JS (Vanilla or Bootstrap optional)

Others:

Maven or Gradle (Build management)

Postman for API testing

Installation
Prerequisites

Java 17+

Maven or Gradle

MySQL or H2 Database

Steps

Clone the repository:

git clone https://github.com/yourusername/smart-contact-manager.git
cd smart-contact-manager


Update database credentials in src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/smartcontact
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update


Build and run the application:

mvn spring-boot:run


Open your browser at: http://localhost:8080

Usage

Open the application in the browser.

Sign up or log in with your credentials.

Add new contacts using the “Add Contact” button.

Edit or delete contacts using the action buttons next to each contact.

Use the search bar to quickly filter contacts.

Pagination is available for long lists of contacts.
