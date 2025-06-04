--liquibase formatted sql

--changeset indy:create-table-transactions

CREATE TABLE IF NOT EXISTS public.transactions (
    id BIGSERIAL PRIMARY KEY,
    account_id VARCHAR NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    type VARCHAR NOT NULL,
    status VARCHAR NOT NULL,
    notification_sent BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL
);

--rollback
DROP TABLE IF EXISTS public.transactions;
