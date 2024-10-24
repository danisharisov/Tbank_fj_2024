--liquibase formatted sql

--changeset danisharisov:create-events
CREATE TABLE events (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        name VARCHAR(255) NOT NULL,
                        date DATE NOT NULL,
                        location_id UUID,
                        CONSTRAINT fk_location_event FOREIGN KEY (location_id) REFERENCES locations (id) ON DELETE CASCADE
);