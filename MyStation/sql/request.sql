create view mouvement_compte as 
select mouvement.id, compte.id as idcompte, compte.num_compte, debit, credit, date, EXTRACT(YEAR FROM date) AS annee
from compte join mouvement on compte.id = mouvement.idcompte;

CREATE VIEW detail_mouvement_compte AS
SELECT 
    num_compte, 
    EXTRACT(YEAR FROM m.date) AS annee, 
    SUM(m.debit) AS totaldebit, 
    SUM(m.credit) AS totalcredit, 
    SUM(m.debit) - SUM(m.credit) AS diff, 
    ABS(SUM(m.debit) - SUM(m.credit)) AS absdiff
FROM mouvement AS m
JOIN compte AS c ON m.idcompte = c.id
GROUP BY num_compte, annee;

CREATE VIEW Prelevement_produit_cuve AS
SELECT pr.valeur_compteur,pr.date,p.id as idpompe,c.id as idcuve,pu.prix_unitaire
FROM prelevement AS pr
join pompe AS p
ON pr.idpompe=p.id 
JOIN cuve AS c
ON p.idcuve = c.id 
JOIN produit as pu
ON c.idproduit = pu.id
GROUP BY pr.valeur_compteur,pr.date,p.id,c.id,pu.prix_unitaire;

select sum(diff) as totalvente from detail_mouvement_compte where num_compte like '7%' and annee=2024;
select sum(diff) as totalvente from detail_mouvement_compte where num_compte like '6%'and annee=2024;

-- Dette
select num_compte, sum(debit)-sum(credit) as reste, annee
from mouvement_compte group by num_compte, annee having num_compte like '411%';

-- Créance
SELECT num_compte, SUM(credit) - SUM(debit) AS reste, annee
FROM mouvement_compte
GROUP BY num_compte, annee
HAVING num_compte LIKE '401%';