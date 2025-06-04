--liquibase formatted sql

--changeset indy:create-table-accounts

CREATE TABLE IF NOT EXISTS public.accounts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    currency VARCHAR(3) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL
);

-- Add unique constraint
ALTER TABLE public.accounts
    ADD CONSTRAINT user_currency_unique UNIQUE (user_id, currency);

--rollback
DROP TABLE IF EXISTS public.accounts;

--changeset indy:create-account-foreign-key-user_id

ALTER TABLE public.accounts
    ADD CONSTRAINT fk_accounts_user_id
    FOREIGN KEY (user_id)
    REFERENCES public.users(id)
    ON DELETE CASCADE;

--rollback
ALTER TABLE public.accounts
    DROP CONSTRAINT fk_accounts_user_id;
