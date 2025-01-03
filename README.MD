# Puppies API

## Project Setup

### Prerequisites

- Docker
- Docker Compose
- Java 17
- Maven

### Steps to Get It Running

1. **Clone the repository:**

   ```sh
   git clone https://github.com/yourusername/puppies-api.git
   cd puppies-api 
    ```
   
2. **Build the Docker images and start the containers:**  
   ```sh  
   docker-compose up --build
    ```
   This will build the Docker image for the Spring Boot application and start the application along with a PostgreSQL database.

3. **Access the application:**
   The application will be accessible at http://localhost:8080.

4. **Steps to Run the Tests**
   Run the tests using Maven:
   ```sh  
   ./mvnw test
    ```
   This will execute all the unit tests in the project.

5. **Highlights**
   - Comprehensive Unit Tests: The project includes comprehensive unit tests for the PostController, PostService, AuthService, and JwtService classes, ensuring the core functionalities are well-tested.
   - Docker Integration: The project is containerized using Docker, making it easy to set up and run the application along with its dependencies.
   - Clean Architecture: The project follows a clean architecture with well-defined layers and separation of concerns, making it easy to maintain and extend.
   
6. **Caveats and Limitations**
   - Error Handling: The current implementation has basic error handling. It could be improved by adding more specific exception handling and custom error responses.
   - Scalability: The application is designed for simplicity and may require additional optimizations and configurations to handle high traffic and large datasets.
   - Logging: The project uses basic logging configurations. For production use, it is recommended to enhance logging with structured logs, log rotation, and log aggregation.
   - Security: The project includes basic JWT-based authentication. For production use, additional security measures such as HTTPS, CSRF protection, and input validation should be implemented. Some endpoints may execute things using user id as parameter, but it could be just using the logged user as well.
   - API Documentation: The project does not include API documentation. It is recommended to add Swagger or other API documentation tools to provide detailed information about the endpoints and request/response formats.
   - Tests: The project includes unit tests for the core functionalities. It is recommended to add integration tests, end-to-end tests, and performance tests to ensure the application works as expected in different scenarios.     

7. **API calls**
   - **Create a new user:**
     ```sh
     curl --request POST --url http://localhost:8080/api/auth/register --header 'Content-Type: application/json' --data '{ "name": "John Doe", "email": "john@example.com", "password": "password123" }'     
    
   - **Get a specific user profile**
        ```sh
        curl --request GET --url http://localhost:8080/api/users/1 --header 'Authorization: Bearer token_here' --header 'Content-Type: application/json'

   - **Login / get token:**  
     ```sh
     curl --request POST --url http://localhost:8080/api/auth/login --header 'Content-Type: application/json' --data '{ "email": "john@example.com", "password": "password123" }'
   
   - **Create a new post:**
     ```sh
     curl --request POST --url http://localhost:8080/api/posts --header 'Authorization: Bearer token_here' --header 'Content-Type: application/json' --data '{ "imageUrl": "http://example.com/dog.jpg", "content": "This is my pet-post" }'
   
   - **Give a Like to a post:**
     ```sh
     curl --request POST --url http://localhost:8080/api/posts/1/like --header 'Authorization: Bearer token_here' --header 'Content-Type: application/json'
   
   - **Get all posts:**
     ```sh
     curl --request GET --url 'http://localhost:8080/api/posts/feed' --header 'Authorization: Bearer token_here' --header 'Content-Type: application/json'
     
   - **Get a specific post:**
     ```sh
     curl --request GET --url http://localhost:8080/api/posts/1 --header 'Authorization token_here' --header 'Content-Type: application/json'
     
   - **Get posts from a specific user:**
     ```sh
     curl --request GET --url 'http://localhost:8080/api/posts/feed?userId=1' --header 'Authorization: Bearer token_here' --header 'Content-Type: application/json'

   - **Get posts that was liked by a specific user:**
     ```sh
     curl --request GET --url 'http://localhost:8080/api/posts/feed?userId=1&liked=true' --header 'Authorization: Bearer token_here' --header 'Content-Type: application/json'