package model;
import db.MyConnect;
import java.sql.*;
import java.util.*;
public class HistoPrixProduit {
    private int id;
    private Produit produit;
    private double prixUnitaire;
    private java.sql.Timestamp date;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prix_unitaire) {
        this.prixUnitaire = prix_unitaire;
    }

    public java.sql.Timestamp getDate() {
        return date;
    }

    public void setDate(java.sql.Timestamp date) {
        this.date = date;
    }

    public static HistoPrixProduit getById(int id) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        HistoPrixProduit instance = null;

        Connection con = MyConnect.getConnection();
        try {
            String query = "SELECT * FROM histo_prix_produit WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                instance = new HistoPrixProduit();
                instance.setId(rs.getInt("id"));
                instance.setProduit(Produit.getById(rs.getInt("idproduit")));
                instance.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                instance.setDate(rs.getTimestamp("date"));
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
    public static HistoPrixProduit[] getAll() throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<HistoPrixProduit> items = new ArrayList<>();

        try {
            String query = "SELECT * FROM histo_prix_produit order by id asc ";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                HistoPrixProduit item = new HistoPrixProduit();
                item.setId(rs.getInt("id"));
                item.setProduit(Produit.getById(rs.getInt("idproduit")));
                item.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                item.setDate(rs.getTimestamp("date"));
                items.add(item);
            }
        } catch (Exception e) {
            throw e ;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null && !false) con.close();
        }

        return items.toArray(new HistoPrixProduit[0]);
    }
    public void insert(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "INSERT INTO histo_prix_produit (idproduit, prix_unitaire, date) VALUES (?, ?, ?)";
            st = con.prepareStatement(query);
            st.setInt(1, this.produit.getId());
            st.setDouble(2, this.prixUnitaire);
            st.setTimestamp(3, this.date);
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
            String query = "UPDATE histo_prix_produit SET id = ?, idproduit = ?, prix_unitaire = ?, date = ? WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, this.id);
            st.setInt (2, this.produit.getId());
            st.setDouble(3, this.prixUnitaire);
            st.setTimestamp(4, this.date);
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
    public static void deleteById(int id) throws Exception {
            Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        try {
            String query = "DELETE FROM histo_prix_produit WHERE id = ?";
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

// By Jos 