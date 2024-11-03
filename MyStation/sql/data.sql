insert into client values (default, 'Divers', CURRENT_DATE);

insert into produit values (default, 'Essence', 5900);
insert into produit values (default, 'Gasoil', 4900);
insert into produit values (default, 'Petrole', 3150);

insert into cuve values (default, 'CUV001', 1, 50000, 35000);
insert into cuve values (default, 'CUV002', 2, 45000, 42000);
insert into cuve values (default, 'CUV003', 3, 65000, 55000);

insert into estimation values (1, 10, 100);
insert into estimation values (2, 30, 250);
insert into estimation values (3, 50, 340);

insert into pompe values (default, 1);
insert into pompe values (default, 2);
insert into pompe values (default, 3);

insert into pompiste values (default, 'Jean', 'jean', crypt('1234', gen_salt('bf')));
insert into pompiste values (default, 'bob', 'bob', crypt('1234', gen_salt('bf')));

-- Insertion des comptes
INSERT INTO compte (num_compte) VALUES ('700');
INSERT INTO compte (num_compte) VALUES ('600');
INSERT INTO compte (num_compte) VALUES ('301');
INSERT INTO compte (num_compte) VALUES ('302');
INSERT INTO compte (num_compte) VALUES ('303');

INSERT INTO client (nom) VALUES ('Jean Dupont');
INSERT INTO client (nom) VALUES ('Marie Curie');
INSERT INTO client (nom) VALUES ('Paul Martin');

-- Insertion des mouvements pour le compte 700
INSERT INTO mouvement (idcompte, debit, credit) VALUES 
(1, 1000.00, 0), -- Débit de 1000 pour le compte 700
(1, 500.00, 0),  -- Débit de 500 pour le compte 700
(1, 0, 1500.00); -- Crédit de 1500 pour le compte 700

-- Insertion des mouvements pour le compte 600
INSERT INTO mouvement (idcompte, debit, credit) VALUES 
(2, 2000.00, 0), -- Débit de 2000 pour le compte 600
(2, 0, 500.00),  -- Crédit de 500 pour le compte 600
(2, 0, 2500.00); -- Crédit de 2500 pour le compte 600