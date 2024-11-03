package model;

import db.MyConnect;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class PrelevementCuve {
    private int id;
    private static Cuve cuve;
    private int idPompe;
    private Pompiste pompiste;
    private double valeurCuve;
    private double quantite;
    private double compteur;
    private java.sql.Timestamp date;

    public int getIdPompe() {
        return idPompe;
    }

    public void setIdPompe(int idPompe) {
        this.idPompe = idPompe;
    }

    public double getCompteur() {
        return compteur;
    }

    public void setCompteur(double compteur) {
        this.compteur = compteur;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cuve getCuve() {
        return cuve;
    }

    public void setCuve(Cuve cuvee) {
        PrelevementCuve.cuve = cuvee;
    }

    public Pompiste getPompiste() {
        return pompiste;
    }

    public void setPompiste(Pompiste pompiste) {
        this.pompiste = pompiste;
    }

    public double getValeurCuve() {
        return valeurCuve;
    }

    public void setValeurCuve(double valeur_cuve) {
        this.valeurCuve = valeur_cuve;
    }

    public java.sql.Timestamp getDate() {
        return date;
    }

    public void setDate(java.sql.Timestamp date) {
        this.date = date;
    }

    public void setDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        // Convertir LocalDate en LocalDateTime en ajoutant minuit comme heure
        LocalDateTime localDateTime = localDate.atStartOfDay();
        // Convertir LocalDateTime en Timestamp
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        this.date = timestamp;
    }

    public static PrelevementCuve[] getAll() throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<PrelevementCuve> items = new ArrayList<>();

        try {
            String query = "SELECT p.*,pm.id as idpompe FROM prelevementCuve p join pompe pm on p.idcuve=pm.idcuve ";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                PrelevementCuve item = new PrelevementCuve();
                item.setId(rs.getInt("id"));
                item.setCuve(model.Cuve.getById(rs.getInt("idCuve")));
                item.setPompiste(Pompiste.getById(rs.getInt("idpompiste")));
                item.setValeurCuve(rs.getDouble("mesure"));
                item.setQuantite(rs.getDouble("quantite"));
                item.setDate(rs.getTimestamp("date"));
                item.setCompteur(Prelevement.getDernierCompteur(rs.getInt("idpompe"))[0]);
                items.add(item);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null)
                rs.close();
            if (st != null)
                st.close();
            if (con != null && !false)
                con.close();
        }

        return items.toArray(new PrelevementCuve[0]);
    }

    public void insert(Connection con) throws Exception {
        PreparedStatement st = null;
        double valeur = Mesure.convertir(this.valeurCuve);

        try {
            String query = "INSERT INTO prelevementCuve (idCuve, idpompiste ,mesure ,quantite, date) VALUES (?, ?, ?, ?, ?)";
            st = con.prepareStatement(query);
            st.setInt(1, PrelevementCuve.cuve.getId());
            st.setInt(2, this.pompiste.getId());
            st.setDouble(3, this.valeurCuve);
            st.setDouble(4, valeur);
            st.setTimestamp(5, this.date);
            try {
                st.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new Exception("Failed to insert record", e);
            }
        } finally {
            if (st != null)
                st.close();
        }
    }

    public static void deleteById(int id) throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        try {
            String query = "DELETE FROM prelevementcuve WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, id);
            try {
                st.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new Exception("Failed to delete record", e);
            }
        } finally {
            if (st != null)
                st.close();
            if (con != null)
                con.close();
        }
    }
}

// By Jos