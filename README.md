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
- API Inter-Communication: RestTemplate
- Other tools: Postman

## Setup Instructions
1. Clone the repository to your local machine:
git clone https://https://github.com/Aanshikgupta/User-Account-Management-System-Microservice.git
2. Create the 'users' table in the 'pet-project-user-service' database with the following SQL query:
## Users Table

The `users` table stores information about registered users.

| Column Name | Data Type | Constraints |
| ----------- | --------- | ----------- |
| user_id | VARCHAR(36) | NOT NULL, UNIQUE |
| user_name | VARCHAR(50) | |
| user_mobile | VARCHAR(10) | |
| user_dob | DATE | |

- `user_id`: Unique identifier for each user.
- `user_name`: Name of the user.
- `user_mobile`: Mobile number of the user.
- `user_dob`: Date of birth of the user.
- `user_id` : Primary Key

3. Create the 'accounts' table in the 'pet-project-account-service' database with the following SQL query:

## Accounts Table

The `accounts` table stores information about registered users.

| Column Name | Data Type | Constraints |
| ----------- | --------- | ----------- |
| account_id | VARCHAR(36) | NOT NULL, UNIQUE |
| user_id | VARCHAR(36) |NOT NULL |
| balance | LONG | |

- `user_id`: Unique identifier for each user.
- `account_id`: Unique identifier for each account.
- `balance`: Balance in the account.
- `account_id` : Primary Key


4. Run the Redis server on localhost:6379.

5. You can access the User Service on localhost:8080.

6. You can access the Account Service on localhost:8081.

7. API Gateway helps to access all the services on localhost:8082.

8. Access the application in your browser.


## Microservices Architecture

The User Account Management System is built using microservices architecture. It comprises two microservices - User and Account - which communicate with each other using RestTemplate.

- **User Microservice**: Provides functionalities for creating, updating, and deleting user accounts, creating account for a user with automatic account creation on user creation.
- **Account Microservice**: Provides functionalities for creating, updating,deleting account information and withdraw and desposit balance.
- **API Gateway Microservice**: Provides functionalities for calling all microservices on the same url & port number.
- **Service Registry Microservice**: Provides functionalities inter-communication between microservices using the name not by port & url. This removes dependency of url and port so that we can change that as per our requirement without affecting the existing system.
- **Config Server Microservice**: Provides functionalities for retrieving common configuration from a git file.




## Conclusion

This project is a part of my end-training project given by my manager(Ms. Heena kalra - IMS Team).The postman collection for the project is also attached.I have applied most of the concepts that I learnt throught-out my training.
