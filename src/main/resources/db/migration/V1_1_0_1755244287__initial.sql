CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(45) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL,
    roles VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    age INTEGER NOT NULL,
    gender TEXT CHECK ( gender IN ('Male', 'Female', 'Prefer Not To Say')),
    bio TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
