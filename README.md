
# Smart Contact Manager

Smart Contact Manager is a web application designed to efficiently manage contacts. This project is built using modern web technologies and frameworks to provide a robust and scalable solution.

## Features

- **User Authentication**: Secure login and registration system.
- **Contact Management**: Add, edit, delete, and search contacts.
- **Responsive Design**: User-friendly interface that works on all devices.
- **Search Functionality**: Easily search for contacts using the search bar.

## Tech Stack

- **Java**: The core programming language used for developing the backend.
- **Spring Boot**: Used for creating the backend RESTful web services.
- **Thymeleaf**: Template engine for server-side rendering of HTML.
- **Hibernate**: ORM framework for database operations.
- - **JUnit**: Writing unit tests for all modules.
- - **SLF4J**: Logging.
- - **SonarQube**: Inspection of code quality.
- **MySQL**: Database to store user and contact information.
- **Maven**: Dependency management and build automation tool.
- **HTML/CSS**: For structuring and styling the web pages.
- **Bootstrap**: CSS framework for responsive design.
- **JavaScript**: Enhancing user interaction and front-end functionality.

## Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/SamraZafar-SZ/Smart-Contact-Manager.git
   cd Smart-Contact-Manager
   ```

2. **Configure the Database:**
   - Create a MySQL database named `smart_contact_manager`.
   - Update the `application.properties` file with your MySQL database username and password.

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/smart_contact_manager
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Build the Project:**
   ```bash
   mvn clean install
   ```

4. **Run the Application:**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application:**
   - Open your web browser and go to `http://localhost:8080`.

## Folder Structure

- **src/main/java**: Contains the Java source files.
- **src/main/resources**: Contains the application properties and static resources.
- **src/main/resources/templates**: Contains Thymeleaf templates for the web pages.
- **src/main/resources/static**: Contains static assets like CSS, JS, and images.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

