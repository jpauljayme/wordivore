CREATE TABLE IF NOT EXISTS book (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    authors TEXT[] DEFAULT '{}'::TEXT[],
    publication_date INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    subjects TEXT[] DEFAULT '{}'::TEXT[],
    pages INTEGER DEFAULT 0,
    isbn_10 VARCHAR(10) DEFAULT '',
    user_id BIGINT REFERENCES users(id),
    COVER_URL TEXT DEFAULT '',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_book_created_at ON book(created_at DESC);