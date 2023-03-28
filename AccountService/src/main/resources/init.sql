CREATE TABLE accounts
(
    user_id    varchar(100),
    account_id varchar(100) NOT NULL UNIQUE,
    balance    long,
    PRIMARY KEY (account_id),
    FOREIGN KEY (user_id) REFERENCES `pet-project-user-service`.users (user_id) ON DELETE CASCADE
);