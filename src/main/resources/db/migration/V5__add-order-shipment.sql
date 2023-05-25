drop table if exists beer_order_shipment;

create table beer_order_shipment (
    id varchar(36) not null,
    beer_order_id varchar(36) unique,
    tracking_number varchar(50),
    created_date datetime(6),
    last_modified_date datetime(6),
    version bigint,
    primary key (id),
    constraint foreign key (beer_order_id) references beer_order (id)
) engine=InnoDB;

alter table beer_order
    add column beer_order_shipment_id varchar(36);

alter table beer_order
    add constraint foreign key (beer_order_shipment_id) references beer_order_shipment (id);