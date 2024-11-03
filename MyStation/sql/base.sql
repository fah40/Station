create database station;
\c station;
CREATE EXTENSION pgcrypto;

CREATE TABLE produit (
    id SERIAL PRIMARY KEY,
    name VARCHAR(35),
    prix_unitaire FLOAT NOT NULL CHECK (prix_unitaire > 0)
);

CREATE TABLE histo_prix_produit (
    id SERIAL PRIMARY KEY,
    idproduit INT REFERENCES produit(id) NOT NULL,
    prix_unitaire FLOAT NOT NULL CHECK (prix_unitaire > 0),
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cuve (
    id SERIAL PRIMARY KEY,
    num_cuve VARCHAR(35),
    idproduit INT REFERENCES produit(id) NOT NULL,
    capacite DOUBLE PRECISION NOT NULL CHECK (capacite > 0),
    quantite DOUBLE PRECISION NOT NULL CHECK (quantite > 0)
);

CREATE TABLE estimation (
    id SERIAL PRIMARY KEY,
    mesure DOUBLE PRECISION NOT NULL CHECK (mesure > 0),
    quantite DOUBLE PRECISION NOT NULL CHECK (quantite > 0)
);

CREATE TABLE pompe (
    id SERIAL PRIMARY KEY,
    idcuve INT REFERENCES cuve(id) NOT NULL
);

CREATE TABLE pompiste (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    username VARCHAR(50),
    mdp VARCHAR(255)
);

CREATE TABLE entree (
    id SERIAL PRIMARY KEY,
    idcuve INT REFERENCES cuve(id) NOT NULL,
    quantite DOUBLE PRECISION NOT NULL CHECK (quantite > 0),
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    prix_unitaire FLOAT NOT NULL CHECK (prix_unitaire > 0)
);

CREATE TABLE sortie (
    id SERIAL PRIMARY KEY,
    idcuve INT REFERENCES cuve(id) NOT NULL,
    quantite DOUBLE PRECISION NOT NULL CHECK (quantite > 0),
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    prix_unitaire FLOAT NOT NULL CHECK (prix_unitaire > 0)
);

CREATE TABLE prelevement (
    id SERIAL PRIMARY KEY,
    idpompe INT REFERENCES pompe(id) NOT NULL,
    idpompiste INT REFERENCES pompiste(id) NOT NULL,
    idprelevementanterieur int references prelevement(id),
    valeur_compteur DOUBLE PRECISION NOT NULL CHECK (valeur_compteur > 0),
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE prelevementCuve (
    id SERIAL PRIMARY KEY,
    idcuve INT REFERENCES pompe(id) NOT NULL,
    idpompiste INT REFERENCES pompiste(id) NOT NULL,
    mesure DOUBLE PRECISION NOT NULL CHECK (mesure > 0),
    quantite DOUBLE PRECISION NOT NULL CHECK (quantite > 0),
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE compte (
    id SERIAL PRIMARY KEY,
    num_compte varchar(10),
    description varchar(100)
);

CREATE TABLE mouvement (
    id serial primary key,
    idcompte int references compte(id) not null,
    description text,
    debit double precision not null default 0,
    credit double precision not null default 0,
    date TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE encaissement (
    id serial primary key,
    montant DOUBLE PRECISION not null check (montant > 0),
    paye DOUBLE PRECISION not null check (paye > 0),
    quantite DOUBLE PRECISION not null,
    idcompte int references compte(id) not null,
    date TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE paiement (
    id serial primary key,
    montant DOUBLE PRECISION not null check (montant > 0),
    paye DOUBLE PRECISION not null check (paye > 0), 
    identree int references entree(id) not null,
    idcompte int references compte(id) not null,
    date TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE client (
    id serial primary key,
    nom VARCHAR(50) not null
);

CREATE TABLE creance (
    id serial primary key,
    idClient int references client(id),
    montant DOUBLE PRECISION not null check (montant > 0),
    date TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    datepa TIMESTAMP not null
);


-- select num_compte, sum(debit)-sum(credit) as reste, annee
-- from mouvement_compte group by num_compte, annee having num_compte like '411%';

-- SELECT num_compte, SUM(credit) - SUM(debit) AS reste, annee
-- FROM mouvement_compte
-- GROUP BY num_compte, annee
-- HAVING num_compte LIKE '401%';