CREATE TABLE IF NOT EXISTS edition (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    work_id BIGINT NOT NULL REFERENCES work(id) ON DELETE CASCADE,
    authors TEXT[] DEFAULT '{}'::TEXT[],
    by_statement TEXT,
    publication_date INTEGER NOT NULL,
    publishers TEXT[] DEFAULT '{}'::TEXT[],
    title VARCHAR(200) NOT NULL,
    pages INTEGER DEFAULT 0,
    isbn_10 VARCHAR(10) DEFAULT '',
    isbn_13 VARCHAR(13) DEFAULT '',
    edition_name TEXT DEFAULT '',
    cover_url TEXT DEFAULT '',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT uk_edition_isbn10 UNIQUE (isbn_10),
    CONSTRAINT uk_edition_isbn13 UNIQUE (isbn_13)
);

CREATE INDEX IF NOT EXISTS idx_edition_work ON edition(work_id);
CREATE INDEX IF NOT EXISTS idx_edition_isbn10 ON edition(isbn_10);
CREATE INDEX IF NOT EXISTS idx_edition_isbn13 ON edition(isbn_13);