package model;

import db.MyConnect;
import java.sql.*;
import java.util.*;

public class Cuve {
    private int id;
    private String numCuve;
    private Produit produit;
    private double capacite;
    private double quantite;

    // Getters et Setters pour les attributs de la classe
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumCuve() {
        return numCuve;
    }

    public void setNumCuve(String num_cuve) {
        this.numCuve = num_cuve;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public double getCapacite() {
        return capacite;
    }

    public void setCapacite(double capacite) {
        this.capacite = capacite;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite (double quantite) {        
        this.quantite = quantite;
    }

    // Méthode pour récupérer un objet Cuve par ID
    public static Cuve getById(int id) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        Cuve instance = null;

        Connection con = MyConnect.getConnection();
        try {
            String query = "SELECT * FROM cuve WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                instance = new Cuve();
                instance.setId(rs.getInt("id"));
                instance.setNumCuve(rs.getString("num_cuve"));
                instance.setProduit(Produit.getById(rs.getInt("idproduit")));
                instance.setCapacite(rs.getDouble("capacite"));  // Récupérer la capacité
                instance.setQuantite(rs.getDouble("quantite"));
            }
        } catch (Exception e) {
            throw e ;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null && !false) con.close();
        }

        return instance;
    }

    // Méthode pour récupérer tous les objets Cuve
    public static Cuve[] getAll() throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Cuve> items = new ArrayList<>();

        try {
            String query = "SELECT * FROM cuve ORDER BY id ASC";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                Cuve item = new Cuve();
                item.setId(rs.getInt("id"));
                item.setNumCuve(rs.getString("num_cuve"));
                item.setProduit(Produit.getById(rs.getInt("idproduit")));
                item.setCapacite(rs.getDouble("capacite"));  // Récupérer la capacité
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

        return items.toArray(new Cuve[0]);
    }

    // Méthode pour insérer une nouvelle cuve
    public void insert(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "INSERT INTO cuve (num_cuve, idproduit, capacite, quantite) VALUES (?, ?, ?, ?)";
            st = con.prepareStatement(query);
            st.setString(1, this.numCuve);
            st.setInt(2, this.produit.getId());
            st.setDouble(3, this.capacite);  // Insérer la capacité
            st.setDouble(4, this.quantite);
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

    // Méthode pour mettre à jour une cuve existante
    public void update(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "UPDATE cuve SET num_cuve = ?, idproduit = ?, capacite = ?, quantite = ? WHERE id = ?";
            st = con.prepareStatement(query);
            st.setString(1, this.numCuve);
            st.setInt(2, this.produit.getId());
            st.setDouble(3, this.capacite);  // Mettre à jour la capacité
            st.setDouble(4, this.quantite);
            st.setInt(5, this.getId());
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

    // Méthode pour supprimer une cuve par ID
    public static void deleteById(int id) throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        try {
            String query = "DELETE FROM cuve WHERE id = ?";
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
