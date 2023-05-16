CREATE TABLE IF NOT EXISTS endpoint_hits(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app       VARCHAR(64) NOT NULL,
    uri       VARCHAR(64) NOT NULL,
    ip        VARCHAR(64) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);