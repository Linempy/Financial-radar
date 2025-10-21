CREATE TABLE IF NOT EXISTS rules (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    name varchar(64) NOT NULL,
    description VARCHAR(512),
    enabled boolean NOT NULL,
    rule_type varchar(20) NOT NULL,
    priority int NOT NULL,
    expression TEXT NOT NULL
);