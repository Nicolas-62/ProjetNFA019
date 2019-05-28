DROP table if exists Medecin;
CREATE TABLE Medecin(
    id smallint(2) not null AUTO_INCREMENT,
    Nom varchar(20) not null,
    constraint pk_id primary key (id)
);