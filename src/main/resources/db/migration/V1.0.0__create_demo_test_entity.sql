
-- create schema if not exists demo;

create sequence test_seq start with 1 increment by 1;

create table IF NOT EXISTS test
(
    id integer not null primary key default nextval('test_seq'),
    name varchar(500)
    );

alter table test
    owner to train_000_user;

-- insert into test (id, name) values(1, 'name-1');