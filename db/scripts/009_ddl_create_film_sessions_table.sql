create table film_sessions
(
    id         serial primary key,
    film_id    int references films (id),
    halls_id   int references halls (id),
    start_time timestamp,
    end_time   timestamp
);