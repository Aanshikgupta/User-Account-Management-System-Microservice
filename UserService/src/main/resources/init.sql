CREATE TABLE users(
                      user_id varchar(100) NOT NULL UNIQUE ,
                      user_name varchar(50),
                      user_mobile varchar(10),
                      user_dob Date,
                      PRIMARY KEY (user_id));