--liquibase formatted sql

--changeset indy:create-transactions-table

CREATE TABLE IF NOT EXISTS public.transactions (
    id BIGSERIAL PRIMARY KEY,
    account_id VARCHAR NOT NULL,
    receiver_account_id VARCHAR NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    transaction_type VARCHAR NOT NULL,
    transaction_status VARCHAR NOT NULL,
    notification_sent BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL
);

--rollback
DROP TABLE IF EXISTS public.transactions;
