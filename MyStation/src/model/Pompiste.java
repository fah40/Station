package model;
import db.MyConnect;
import java.sql.*;
import java.util.*;
public class Pompiste {
    private int id;
    private String nom;
    private String username;
    private String mdp;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public static Pompiste getById(int id) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        Pompiste instance = null;

        Connection con = MyConnect.getConnection();
        try {
            String query = "SELECT * FROM pompiste WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                instance = new Pompiste();
                instance.setId(rs.getInt("id"));
                instance.setNom(rs.getString("nom"));
                instance.setUsername(rs.getString("username"));
                instance.setMdp(rs.getString("mdp"));
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
    public static Pompiste[] getAll() throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Pompiste> items = new ArrayList<>();

        try {
            String query = "SELECT * FROM pompiste order by id asc ";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                Pompiste item = new Pompiste();
                item.setId(rs.getInt("id"));
                item.setNom(rs.getString("nom"));
                item.setUsername(rs.getString("username"));
                item.setMdp(rs.getString("mdp"));
                items.add(item);
            }
        } catch (Exception e) {
            throw e ;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null && !false) con.close();
        }

        return items.toArray(new Pompiste[0]);
    }
    public void insert(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "INSERT INTO pompiste (nom, username, mdp) VALUES (?, ?, ?)";
            st = con.prepareStatement(query);
            st.setString(1, this.nom);
            st.setString(2, this.username);
            st.setString(3, this.mdp);
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
            String query = "UPDATE pompiste SET id = ?, nom = ?, username = ?, mdp = ? WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, this.id);
            st.setString(2, this.nom);
            st.setString(3, this.username);
            st.setString(4, this.mdp);
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
            String query = "DELETE FROM pompiste WHERE id = ?";
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

    public static Pompiste login (String username, String mdp) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        Pompiste instance = null;

        Connection con = MyConnect.getConnection();
        try {
            String query = "SELECT * FROM pompiste WHERE username = ? AND mdp = crypt(?, mdp)";
            st = con.prepareStatement(query);
            st.setString(1, username);
            st.setString(2, mdp);
            rs = st.executeQuery();

            if (rs.next()) {
                instance = new Pompiste();
                instance.setId(rs.getInt("id"));
                instance.setNom(rs.getString("nom"));
                instance.setUsername(rs.getString("username"));
                instance.setMdp(rs.getString("mdp"));
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
}

// By Jos 