DO $$
BEGIN
    CREATE TYPE shelf_status AS ENUM ('TO_READ','CURRENTLY_READING','READ','DNF');
EXCEPTION
    WHEN duplicate_object THEN NULL;
END$$;

CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    body TEXT,
    rating SMALLINT NOT NULL CHECK (rating BETWEEN 1 AND 5),

    edition_id BIGINT NULL REFERENCES edition(id) ON DELETE SET NULL,
    work_id BIGINT NOT NULL REFERENCES work(id)   ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id)  ON DELETE CASCADE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_review_user_work UNIQUE (user_id, work_id)
);

CREATE INDEX IF NOT EXISTS idx_reviews_user_id ON reviews(user_id);
CREATE INDEX IF NOT EXISTS idx_reviews_edition_id ON reviews(edition_id);
CREATE INDEX IF NOT EXISTS idx_reviews_work_id ON reviews(work_id);

CREATE TABLE IF NOT EXISTS library_item (
    id BIGINT GENERATED ALWAYS  AS IDENTITY PRIMARY KEY,
    user_id BIGINT              NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    edition_id BIGINT           NOT NULL REFERENCES edition(id) ON DELETE CASCADE,
    status shelf_status,

    read_start DATE,
    read_end DATE,

    created_at TIMESTAMPTZ              NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_user_edition_id      UNIQUE (user_id, edition_id)
);

CREATE INDEX IF NOT EXISTS idx_lib_user ON library_item(user_id);
CREATE INDEX IF NOT EXISTS idx_lib_edition ON library_item(edition_id);
CREATE INDEX IF NOT EXISTS idx_lib_created_at ON library_item(created_at);