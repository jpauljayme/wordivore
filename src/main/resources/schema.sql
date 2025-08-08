--CREATE TYPE IF NOT EXISTS AS ENUM ('M', 'F');
--
--CREATE TYPE role IF NOT EXISTS ENUM ('ROLE_ADMIN', 'ROLE_USER');

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(45) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL,
    age INTEGER NOT NULL,
    sex sex_type,
    bio TEXT
);



CREATE TABLE IF NOT EXISTS authorities (
    id SERIAL PRIMARY KEY,
    username VARCHAR(45) NOT NULL,
    authority role NOT NULL,
    UNIQUE(username, authority)
);