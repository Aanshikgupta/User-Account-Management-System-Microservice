CREATE TABLE accounts
(
    user_id    varchar(36) NOT NULL,
    account_id varchar(36),
    balance    long,
    PRIMARY KEY (account_id),
    FOREIGN KEY (user_id) REFERENCES `pet-project-user-service`.users (user_id) ON DELETE CASCADE
);