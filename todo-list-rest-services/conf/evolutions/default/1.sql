# --- !Ups

create table task (
  id                            integer not null,
  name                          varchar(255),
  complete                      boolean default false not null,
  constraint pk_task primary key (id)
);


# --- !Downs

drop table if exists task;

