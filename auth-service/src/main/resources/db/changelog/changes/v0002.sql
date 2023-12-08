CREATE TABLE "refresh_token"
(
    id          bigserial   not null,
    token       varchar(255) not null,
    user_id     bigserial not null,
    expiry_date timestamp   not null,
    primary key (id),
    foreign key (user_id) references "user_tbl"(user_id)
);