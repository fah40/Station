package model;
import db.MyConnect;
import java.sql.*;
import java.util.*;
public class Entree {
    private int id;
    private Cuve cuve;
    private double quantite;
    private java.sql.Timestamp date;
    private double prixUnitaire;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cuve getCuve() {
        return cuve;
    }

    public void setCuve(Cuve cuve) {
        this.cuve = cuve;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public java.sql.Timestamp getDate() {
        return date;
    }

    public void setDate(java.sql.Timestamp date) {
        this.date = date;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prix_unitaire) {
        this.prixUnitaire = prix_unitaire;
    }

    public static Entree getById(int id) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        Entree instance = null;

        Connection con = MyConnect.getConnection();
        try {
            String query = "SELECT * FROM entree WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                instance = new Entree();
                instance.setId(rs.getInt("id"));
                instance.setCuve(Cuve.getById(rs.getInt("idcuve")));
                instance.setQuantite(rs.getDouble("quantite"));
                instance.setDate(rs.getTimestamp("date"));
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
    public static Entree[] getAll() throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Entree> items = new ArrayList<>();

        try {
            String query = "SELECT * FROM entree order by id asc ";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                Entree item = new Entree();
                item.setId(rs.getInt("id"));
                item.setCuve(Cuve.getById(rs.getInt("idcuve")));
                item.setQuantite(rs.getDouble("quantite"));
                item.setDate(rs.getTimestamp("date"));
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

        return items.toArray(new Entree[0]);
    }
    public void insert(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "INSERT INTO entree (idcuve, quantite, date, prix_unitaire) VALUES (?, ?, ?, ?)";
            st = con.prepareStatement(query);
            st.setInt(1, this.cuve.getId());
            st.setDouble(2, this.quantite);
            st.setTimestamp(3, this.date);
            st.setDouble(4, this.prixUnitaire);
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
            String query = "UPDATE entree SET id = ?, idcuve = ?, quantite = ?, date = ?, prix_unitaire = ? WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, this.id);
            st.setInt (2, this.cuve.getId());
            st.setDouble(3, this.quantite);
            st.setTimestamp(4, this.date);
            st.setDouble(5, this.prixUnitaire);
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
            String query = "DELETE FROM entree WHERE id = ?";
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