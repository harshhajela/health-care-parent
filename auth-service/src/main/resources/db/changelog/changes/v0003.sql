CREATE TABLE user_activation (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGSERIAL NOT NULL,
     token VARCHAR(255) NOT NULL,
     created_at TIMESTAMP NOT NULL,
     expired_at TIMESTAMP NOT NULL,
     confirmed_at TIMESTAMP,
     foreign key (user_id) references "user_tbl"(user_id)
);