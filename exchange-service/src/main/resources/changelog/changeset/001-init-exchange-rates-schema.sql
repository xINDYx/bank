--liquibase formatted sql

--changeset indy:create-table-exchange-rates

CREATE TABLE IF NOT EXISTS public.exchange_rates (
    id BIGSERIAL PRIMARY KEY,
    currency VARCHAR UNIQUE NOT NULL,
    rate DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL
);

--rollback
DROP TABLE IF EXISTS public.exchange_rates;
