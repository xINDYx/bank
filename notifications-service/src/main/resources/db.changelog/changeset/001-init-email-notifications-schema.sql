--liquibase formatted sql

--changeset indy:create-table-email-notification

CREATE TABLE IF NOT EXISTS public.email_notification (
    id BIGSERIAL PRIMARY KEY,
    recipient VARCHAR NOT NULL,
    subject VARCHAR NOT NULL,
    message VARCHAR NOT NULL,
    sent BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL
);

--rollback
DROP TABLE IF EXISTS public.email_notification;