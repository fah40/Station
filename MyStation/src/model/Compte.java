package model;

import db.MyConnect;
import java.sql.*;
import java.util.*;

public class Compte {
    private int id;
    private String numCompte;
    private String description;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumCompte() {
        return numCompte;
    }

    public void setNumCompte(String numCompte) {
        this.numCompte = numCompte;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Methods for database operations
    public static Compte getById(int id) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        Compte instance = null;

        Connection con = MyConnect.getConnection();
        try {
            String query = "SELECT * FROM compte WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                instance = new Compte();
                instance.setId(rs.getInt("id"));
                instance.setNumCompte(rs.getString("num_compte"));
                instance.setDescription(rs.getString("description"));
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

    public static Compte getByNum(int num) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        Compte instance = null;

        Connection con = MyConnect.getConnection();
        try {
            String query = "SELECT * FROM compte WHERE num_compte like ?";
            st = con.prepareStatement(query);
            st.setString(1, num +"%");
            rs = st.executeQuery();

            if (rs.next()) {
                instance = new Compte();
                instance.setId(rs.getInt("id"));
                instance.setNumCompte(rs.getString("num_compte"));
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

    public static Compte[] getAll() throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Compte> items = new ArrayList<>();

        try {
            String query = "SELECT * FROM compte ORDER BY id ASC";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                Compte item = new Compte();
                item.setId(rs.getInt("id"));
                item.setNumCompte(rs.getString("num_compte"));
                item.setDescription(rs.getString("description"));
                items.add(item);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null) con.close();
        }

        return items.toArray(new Compte[0]);
    }

    public void insert(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "INSERT INTO compte (num_compte, description) VALUES (?, ?)";
            st = con.prepareStatement(query);
            st.setString(1, this.numCompte);
            st.setString(2, this.description);
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
            String query = "UPDATE compte SET num_compte = ?, description = ? WHERE id = ?";
            st = con.prepareStatement(query);
            st.setString(1, this.numCompte);
            st.setString(2, this.description);
            st.setInt(3, this.getId());
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
            String query = "DELETE FROM compte WHERE id = ?";
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
    public static void main(String[] args) throws Exception {
        Compte c= Compte.getByNum(7);
        System.out.println(c.getId());
    }
}
