# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table diretorio (
  id                            bigserial not null,
  nome                          varchar(255),
  caminho                       varchar(255),
  constraint pk_diretorio primary key (id)
);

create table usuarios (
  id                            bigserial not null,
  nome                          varchar(255),
  lixeira                       varchar(255),
  email                         varchar(255),
  senha                         varchar(255),
  hora_do_login                 timestamp,
  constraint pk_usuarios primary key (id)
);


# --- !Downs

drop table if exists diretorio cascade;

drop table if exists usuarios cascade;

