CREATE DATABASE IF NOT EXISTS gamenav_appdata;
USE gamenav_appdata;

-- overall
create table if not exists headlist(
    headid int not null unique auto_increment primary key,
    name varchar(255)
);

-- steam
create table if not exists steamList(
    headid int unique,
    foreign key (headid) references headlist(headid),
    appID int not null unique primary key, 
    name varchar(255), 
    type varchar(10) not null
);
create table if not exists steamGameDesc(
    appid int not null unique, 
    foreign key (appid) references steamList(appID),
    agerequired boolean,
    gameDesc text, 
    releaseDate varchar(25),
    price int not null, 
    is_free boolean,
    metacritic int 
);

-- epic
create table if not exists epicList(
    headid int unique,
    foreign key (headid) references headlist(headid),
    appID int not null auto_increment primary key, 
    name varchar(255),
    type varchar(10) not null
);

-- for all
create table if not exists languages( --
    headid int not null unique,
    foreign key (headid) references headlist(headid),
    english boolean
);
create table if not exists supportedOS(
    headid int unique,
    foreign key (headid) references headlist(headid),
    windows boolean, 
    mac boolean, 
    linux boolean
);
create table if not exists tags( --
    headid int unique,
    foreign key (headid) references headlist(headid),
    action boolean
);
create table if not exists genres( --
    headid int not null unique,
    foreign key (headid) references headlist(headid),
    action boolean
);
create table if not exists developers( --
    headid int unique,
    foreign key (headid) references headlist(headid),
    valve boolean
);
create table if not exists publishers( --
    headid int unique,
    foreign key (headid) references headlist(headid),
    valve boolean
);