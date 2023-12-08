create table "booking"
(
    booking_id bigserial   not null,
    customer_id integer not null,
    care_provider_id   integer not null,
    created     timestamp   not null,
    primary key (booking_id)
);