package model;

import db.MyConnect;
import java.sql.*;
import java.util.*;

public class Mesure {
    private int id;
    private double valeur;
    private double quantite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public static Mesure[] getAll() throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Mesure> items = new ArrayList<>();

        try {
            String query = "SELECT * FROM estimation";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                Mesure item = new Mesure();
                item.setId(rs.getInt("id"));
                item.setValeur(rs.getDouble("mesure"));
                item.setQuantite(rs.getDouble("quantite"));
                items.add(item);
            }
        } catch (Exception e) {
            throw e ;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null && !false) con.close();
        }

        return items.toArray(new Mesure[0]);
    }

    public static double convertir(double valeur) throws Exception {
        // Chercher les deux points les plus proches de la valeur en cm
        Mesure mesureInf = null;
        Mesure mesureSup = null;

        Mesure[] mesures= Mesure.getAll();

        for (Mesure mesure : mesures) {
            if (mesure.valeur <= valeur) {
                mesureInf = mesure;
            }
            if (mesure.valeur >= valeur && mesureSup == null) {
                mesureSup = mesure;
                break;
            }
        }

        if (mesureInf != null && mesureInf.valeur == valeur) {
            return mesureInf.quantite;
        }
        if (mesureInf != null && mesureSup != null) {
            double deltaValeur = mesureSup.valeur - mesureInf.valeur;
            double deltaQuantite = mesureSup.quantite - mesureInf.quantite;
            double proportion = (valeur - mesureInf.valeur) / deltaValeur;

            return mesureInf.quantite + proportion * deltaQuantite;
        }
        return -1; // Retourne une valeur d'erreur ou une gestion différente selon votre besoin
    }
}
