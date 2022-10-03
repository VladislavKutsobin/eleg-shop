drop table if exists roles;
create table roles (
    id bigint not null auto_increment,
    created timestamp default current_timestamp,
    status varchar(255) default 'ACTIVE',
    updated timestamp default current_timestamp,
    name varchar(255),
    primary key (id)
) engine=InnoDB;

drop table if exists users;
create table users (
    id bigint not null auto_increment,
    created timestamp default current_timestamp,
    status varchar(255) default 'ACTIVE',
    updated timestamp default current_timestamp,
    email varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    password varchar(255),
    primary key (id)
) engine=InnoDB;

drop table if exists user_roles;
create table user_roles (
    user_id bigint not null,
    role_id bigint not null
) engine=InnoDB;

alter table user_roles
    add constraint FKh8ciramu9cc9q3qcqiv4ue8a6
        foreign key (role_id)
            references roles (id);

alter table user_roles
    add constraint FKhfh9dx7w3ubf1co1vdev94g3f
        foreign key (user_id)
            references users (id);

insert into roles(name) values ('ROLE_ADMIN');

insert into roles(name) values ('ROLE_USER');