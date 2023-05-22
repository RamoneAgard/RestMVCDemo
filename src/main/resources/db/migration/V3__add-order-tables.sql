drop table if exists beer_order;

drop table if exists beer_order_line;

create table beer_order (
    id varchar(36) not null,
    customer_id varchar(36),
    customer_ref varchar(255),
    created_date datetime(6),
    last_modified_date datetime(6),
    version bigint,
    primary key (id),
    constraint foreign key (customer_id) references customer (id)
) engine=InnoDB;

create table beer_order_line (
    id varchar(36) not null,
    beer_order_id varchar(36),
    beer_id varchar(36),
    order_quantity integer,
    quantity_allocated integer,
    created_date datetime(6),
    last_modified_date datetime(6),
    version bigint,
    primary key (id),
    constraint foreign key (beer_order_id) references beer_order (id),
    constraint foreign key (beer_id) references beer (id)
) engine=InnoDB;