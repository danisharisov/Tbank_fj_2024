--liquibase formatted sql

--changeset danisharisov:create-locations
CREATE TABLE locations (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           name VARCHAR(255) NOT NULL UNIQUE
);