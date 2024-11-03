package model;
import db.MyConnect;
import java.sql.*;
import java.util.*;
public class Produit {
    private int id;
    private String name;
    private double prixUnitaire;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prix_unitaire) {
        this.prixUnitaire = prix_unitaire;
    }

    public static Produit getById(int id) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        Produit instance = null;

        Connection con = MyConnect.getConnection();
        try {
            String query = "SELECT * FROM produit WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                instance = new Produit();
                instance.setId(rs.getInt("id"));
                instance.setName(rs.getString("name"));
                instance.setPrixUnitaire(rs.getDouble("prix_unitaire"));
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
    public static Produit[] getAll() throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Produit> items = new ArrayList<>();

        try {
            String query = "SELECT * FROM produit order by id asc ";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                Produit item = new Produit();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                items.add(item);
            }
        } catch (Exception e) {
            throw e ;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null && !false) con.close();
        }

        return items.toArray(new Produit[0]);
    }
    public void insert(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "INSERT INTO produit (name, prix_unitaire) VALUES (?, ?)";
            st = con.prepareStatement(query);
            st.setString(1, this.name);
            st.setDouble(2, this.prixUnitaire);
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
            String query = "UPDATE produit SET id = ?, name = ?, prix_unitaire = ? WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, this.id);
            st.setString(2, this.name);
            st.setDouble(3, this.prixUnitaire);
            st.setInt(4, this.getId());
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
            String query = "DELETE FROM produit WHERE id = ?";
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