create table warehouses (
    id int unsigned primary key auto_increment,
    city varchar(100) not null,
    x int,
    y int,
    rice boolean not null,
    pasta boolean not null,
    water boolean not null,
);