# User Account Management System

The User Account Management System is a microservices-based software application designed to manage user accounts in an organization. It provides functionalities for creating, updating, and deleting user accounts, as well as managing access control and user authentication. The application is built using Springboot, MySQL, Mybatis, and Redis for caching.

## Features

- Create and manage user accounts using User microservice
- Create and manage user accounts using Account microservice
- Update user profile information using User microservice
- Update account information using Account microservice
- Delete user accounts using User microservice
- Delete account information using Account microservice
- Manage user authentication and access control
- User-friendly web interface for easy management
- Secure password storage and retrieval
- Audit logs to track user activity

## Technologies Used

- Programming Language: Java
- Framework: Springboot
- Database: MySQL
- ORM Framework: Mybatis
- Caching: Redis
- API Gateway: Netflix Zuul
- Service Registry: Netflix Eureka
- Configuration Server: Spring Cloud Config

## Microservices Architecture

The User Account Management System is built using microservices architecture. It comprises two microservices - User and Account - which communicate with each other using RestTemplate.

- **User Microservice**: Provides functionalities for creating, updating, and deleting user accounts, creating account for a user with automatic account creation on user creation.
- **Account Microservice**: Provides functionalities for creating, updating, and deleting account information.

## Setup Instructions

1. Clone the repository `git clone https://github.com/yourusername/user-account-management.git`
2. Change directory into the project folder `cd user-account-management`
3. Start the Configuration Server `cd config-server && mvn spring-boot:run`
4. Start the Eureka Server `cd service-registry && mvn spring-boot:run`
5. Start the API Gateway `cd api-gateway && mvn spring-boot:run`
6. Start the User Microservice `cd user-microservice && mvn spring-boot:run`
7. Start the Account Microservice `cd account-microservice && mvn spring-boot:run`
8. Access the application in your browser at `http://localhost:8765/`

## Contributing

Contributions to the User Account Management System are welcome. Before contributing, please read the [contributing guide](CONTRIBUTING.md) for more information.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
