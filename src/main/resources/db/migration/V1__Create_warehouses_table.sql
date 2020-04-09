create table warehouses (
    id int unsigned primary key auto_increment,
    city varchar(100) not null,
    x int,
    y int,
    rice int,
    pasta int,
    water int,
);