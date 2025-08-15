CREATE TABLE IF NOT EXISTS book (
    id BIGSERIAL PRIMARY KEY,
    authors VARCHAR(100) NOT NULL,
    publication_date INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    subjects TEXT[] DEFAULT '{}'::TEXT[],
    pages INTEGER NOT NULL,
    isbn_10 TEXT[] DEFAULT '{}'::TEXT[],
    user_id BIGINT REFERENCES users(id)
);