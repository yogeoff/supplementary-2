DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS
(
    id    INT AUTO_INCREMENT  PRIMARY KEY,
    name  VARCHAR(250) NOT NULL,
    email VARCHAR(250) DEFAULT NULL
);


CREATE TABLE CARS
(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(250) NOT NULL,
    image VARCHAR(500) NOT NULL
);

CREATE
USER application PASSWORD 'hacker';
GRANT SELECT ON USERS TO application;
GRANT SELECT ON CARS TO application;