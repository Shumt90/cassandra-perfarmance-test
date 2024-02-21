create keyspace perf with replication = {'class': 'SimpleStrategy', 'replication_factor': 1};


create table perf.t_single
(
    id   int primary key,
    data text
);

create table perf.t_composite
(
    group int,
    id    int,
    data  text,
    primary key ((group), id)
);

create table perf.t_single
(
    id   int primary key,
    data text
)
    WITH compression = { 'enabled' : false };
