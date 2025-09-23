CREATE TABLE IF NOT EXISTS work (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    key TEXT,
    description TEXT,
    first_sentence TEXT,
    subjects TEXT[] DEFAULT '{}'::TEXT[],
    ave_rating NUMERIC (2,1),
    ratings_count BIGINT DEFAULT 0,
    reviews_count BIGINT DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_work_created_at ON work(created_at DESC);