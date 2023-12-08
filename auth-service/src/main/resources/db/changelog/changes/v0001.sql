-- Create the role table
CREATE TABLE "role"
(
    id bigserial    not null,
    name   varchar(255) not null,
    primary key (id)
);

-- Insert roles into the role table
INSERT INTO "role" (name) VALUES ('ADMIN');
INSERT INTO "role" (name) VALUES ('CUSTOMER');
INSERT INTO "role" (name) VALUES ('PROVIDER');

-- Create the user_tbl table with a foreign key column
CREATE TABLE "user_tbl"
(
    user_id     bigserial   not null,
    email       varchar(255) not null,
    password    varchar(255) not null,
    created     timestamp   not null,
    status      varchar(255) not null,
    role_id     bigint,
    primary key (user_id),
    foreign key (role_id) references "role"(id)
);

-- Add a unique constraint on the email column
ALTER TABLE user_tbl ADD CONSTRAINT unique_email UNIQUE (email);
