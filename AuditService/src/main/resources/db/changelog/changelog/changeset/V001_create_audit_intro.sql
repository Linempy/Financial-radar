CREATE TABLE IF NOT EXISTS processing_audit (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    correlation_id varchar(36) NOT NULL,
    service_module varchar(50) NOT NULL,
    status varchar(20) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    total_duration_ms bigint,
    metadata JSONB
);

CREATE TABLE IF NOT EXISTS processing_steps (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    audit_id BIGINT NOT NULL REFERENCES processing_audit(id),
    step_name VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    details VARCHAR(512),
    created_at TIMESTAMP NOT NULL,
    duration_ms BIGINT
);