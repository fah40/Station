package model;

import db.MyConnect;
import java.sql.*;
import java.util.*;

public class Paiement {
    private int id;
    private double montant;
    private double paye;
    private Entree entree;
    private Compte compte;
    private Timestamp date;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public double getPaye() {
        return paye;
    }

    public void setPaye(double paye) {
        this.paye = paye;
    }

    public Entree getEntree() {
        return entree;
    }

    public void setEntree(Entree entree) {
        this.entree = entree;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    // Methods for database operations
    public static Paiement getById(int id) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        Paiement instance = null;

        Connection con = MyConnect.getConnection();
        try {
            String query = "SELECT * FROM paiement WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                instance = new Paiement();
                instance.setId(rs.getInt("id"));
                instance.setMontant(rs.getDouble("montant"));
                instance.setPaye(rs.getDouble("paye"));
                instance.setEntree(Entree.getById(rs.getInt("identree")));
                instance.setCompte(Compte.getById(rs.getInt("idcompte")));
                instance.setDate(rs.getTimestamp("date"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null) con.close();
        }

        return instance;
    }

    public static Paiement[] getAll() throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Paiement> items = new ArrayList<>();

        try {
            String query = "SELECT * FROM paiement ORDER BY id ASC";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                Paiement item = new Paiement();
                item.setId(rs.getInt("id"));
                item.setMontant(rs.getDouble("montant"));
                item.setPaye(rs.getDouble("paye"));
                item.setEntree(Entree.getById(rs.getInt("identree")));
                item.setCompte(Compte.getById(rs.getInt("idcompte")));
                item.setDate(rs.getTimestamp("date"));
                items.add(item);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null) con.close();
        }

        return items.toArray(new Paiement[0]);
    }

    public void insert(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "INSERT INTO paiement (montant, paye, identree, idcompte, date) VALUES (?, ?, ?, ?, ?)";
            st = con.prepareStatement(query);
            st.setDouble(1, this.montant);
            st.setDouble(2, this.paye);
            st.setInt(3, this.entree.getId());
            st.setInt(4, this.compte.getId());
            st.setTimestamp(5, this.date);
            try {
                st.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new Exception("Failed to insert record", e);
            }
        } finally {
            if (st != null) st.close();
        }
    }

    public void update(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "UPDATE paiement SET montant = ?, paye = ?, identree = ?, idcompte = ?, date = ? WHERE id = ?";
            st = con.prepareStatement(query);
            st.setDouble(1, this.montant);
            st.setDouble(2, this.paye);
            st.setInt(3, this.entree.getId());
            st.setInt(4, this.compte.getId());
            st.setTimestamp(5, this.date);
            st.setInt(6, this.getId());
            try {
                st.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new Exception("Failed to update record", e);
            }
        } finally {
            if (st != null) st.close();
        }
    }

    public static void deleteById(int id) throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        try {
            String query = "DELETE FROM paiement WHERE id = ?";
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
            if (st != null) st.close();
            if (con != null) con.close();
        }
    }
}
