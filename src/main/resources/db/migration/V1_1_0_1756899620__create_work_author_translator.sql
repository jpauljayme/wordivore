CREATE EXTENSION IF NOT EXISTS citext;

CREATE TABLE IF NOT EXISTS person (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name citext NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_person_name UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS work_author (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    person_id BIGINT NOT NULL REFERENCES person(id) ON DELETE CASCADE,
    work_id BIGINT NOT NULL REFERENCES work(id) ON DELETE RESTRICT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_work_author UNIQUE (work_id, person_id)
);

CREATE INDEX IF NOT EXISTS idx_work_author_work                     ON work_author(work_id);
CREATE INDEX IF NOT EXISTS idx_work_author_person                   ON work_author(person_id);

CREATE TABLE IF NOT EXISTS edition_translator (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    person_id BIGINT NOT NULL REFERENCES person(id) ON DELETE CASCADE,
    edition_id BIGINT NOT NULL REFERENCES edition(id) ON DELETE RESTRICT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_edition_translator UNIQUE (edition_id, person_id)
);

CREATE INDEX IF NOT EXISTS idx_edtr_edition         ON edition_translator(edition_id);
CREATE INDEX IF NOT EXISTS idx_edtr_person          ON edition_translator(person_id);

CREATE TABLE IF NOT EXISTS edition_contributor (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    person_id BIGINT NOT NULL REFERENCES person(id) ON DELETE CASCADE,
    edition_id BIGINT NOT NULL REFERENCES edition(id) ON DELETE RESTRICT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_edition_contributor UNIQUE (edition_id, person_id)
);

CREATE INDEX IF NOT EXISTS idx_edctbr_edition ON edition_contributor(edition_id);
CREATE INDEX IF NOT EXISTS idx_edctbr_person ON edition_contributor(person_id);
