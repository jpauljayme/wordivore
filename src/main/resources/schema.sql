CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(45) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL,
    roles VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    age INTEGER NOT NULL,
    gender VARCHAR(20),
    bio TEXT
);



CREATE TABLE IF NOT EXISTS authorities (
    id SERIAL PRIMARY KEY,
    username VARCHAR(45) NOT NULL,
    authority role NOT NULL,
    UNIQUE(username, authority)
);