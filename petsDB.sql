drop database if exists petsdam;
create database petsdam;

use petsdam;

create table user(
 email varchar(150) primary key,
 pass varchar(50),
 nick varchar(50)
);

insert into user values("prueba@prueba.es","123123","Prueba");

create table pet(
	name varchar(50),
    vaccinated boolean,
    species varchar(30),
    ownerEmail varchar(150),
    foreign key (ownerEmail) references user(email),
    primary key(name,ownerEmail)
);

insert into pet values("centollo",true,"Bicho palo","prueba@prueba.es");


create table toy(
	id int primary key auto_increment,
    name varchar(30),
    material varchar(40)
);

insert into toy (name,material) values ("pollo de goma","Goma");

create table pettoys(
	toyId int,
    petName varchar(50),	
    petOwner varchar(150),
    primary key(toyId,petName,petOwner),
    foreign key (toyId) references toy(id),
    foreign key (petName) references pet(name),
    foreign key (petOwner) references pet(ownerEmail)
);

insert into pettoys values(1,"centollo","prueba@prueba.es");



