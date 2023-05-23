drop table if exists category;

drop table if exists beer_category;

create table category (
    id varchar(36) not null,
    description varchar(50),
    created_date datetime(6),
    last_modified_date datetime(6),
    version bigint,
    primary key (id)
) engine=InnoDB;

create table beer_category (
    beer_id varchar(36) not null,
    category_id varchar(36) not null,
    primary key (beer_id, category_id),
    constraint foreign key (beer_id) references beer (id),
    constraint foreign key (category_id) references category (id)
) engine=InnoDB;