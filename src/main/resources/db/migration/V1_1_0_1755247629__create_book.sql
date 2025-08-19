CREATE TABLE IF NOT EXISTS book (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    authors TEXT[] DEFAULT '{}'::TEXT[],
    publication_date INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    subjects TEXT[] DEFAULT '{}'::TEXT[],
    pages INTEGER NOT NULL,
    isbn_10 TEXT[] DEFAULT '{}'::TEXT[],
    user_id BIGINT REFERENCES users(id)
);