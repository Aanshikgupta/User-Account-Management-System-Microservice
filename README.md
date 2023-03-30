# User Account Management System

The User Account Management System is a microservices-based software application designed to manage user accounts in an organization. It provides functionalities for creating, updating, and deleting user accounts, as well as managing access control and user authentication. The application is built using Springboot, MySQL, Mybatis, and Redis for caching.

## Features

- Create and manage user accounts using User microservice
- Create and manage user accounts using Account microservice
- Update user profile information using User microservice
- Update account information using Account microservice
- Delete user accounts using User microservice
- Delete account information using Account microservice
- Automatic Account Creation on User Creation
- Account Creation from User API Itself
- One User can have many accounts

## Technologies Used

- Programming Language: Java
- Framework: Springboot
- Database: MySQL
- ORM Framework: Mybatis
- Caching: Redis
- API Gateway: Spring Cloud Gateway
- Service Registry: Netflix Eureka
- Configuration Server: Spring Cloud Config
- Other tools: Postman

## Microservices Architecture

The User Account Management System is built using microservices architecture. It comprises two microservices - User and Account - which communicate with each other using RestTemplate.

- **User Microservice**: Provides functionalities for creating, updating, and deleting user accounts, creating account for a user with automatic account creation on user creation.
- **Account Microservice**: Provides functionalities for creating, updating,deleting account information and withdraw and desposit balance.


## Conclusion

This project is a part of my end-training project given by my manager. I have applied most of the concepts that I learnt throught-out my training.
