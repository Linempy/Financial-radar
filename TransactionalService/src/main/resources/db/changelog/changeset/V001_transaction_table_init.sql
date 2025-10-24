CREATE TABLE transactions (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    correlation_id VARCHAR(36) NOT NULL UNIQUE,
    amount MONEY NOT NULL,
    currency VARCHAR(3) NOT NULL,
    description VARCHAR(512),
    status VARCHAR(32) NOT NULL,
    is_fraud BOOLEAN,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    ip_address VARCHAR(255),
    user_agent TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
