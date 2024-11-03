package model;
import java.sql.*;
import java.util.*;
import db.MyConnect;
public class Pompe {
    private int id;
    private Cuve cuve;
    
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

    public static Pompe getById(int id) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        Pompe instance = null;

        Connection con = MyConnect.getConnection();
        try {
            String query = "SELECT * FROM pompe WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                instance = new Pompe();
                instance.setId(rs.getInt("id"));
                instance.setCuve(Cuve.getById(rs.getInt("idcuve")));
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
    public static Pompe[] getAll() throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Pompe> items = new ArrayList<>();

        try {
            String query = "SELECT * FROM pompe order by id asc ";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                Pompe item = new Pompe();
                item.setId(rs.getInt("id"));
                item.setCuve(Cuve.getById(rs.getInt("idcuve")));
                items.add(item);
            }
        } catch (Exception e) {
            throw e ;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null && !false) con.close();
        }

        return items.toArray(new Pompe[0]);
    }
    public void insert(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "INSERT INTO pompe (idcuve) VALUES (?)";
            st = con.prepareStatement(query);
            st.setInt(1, this.cuve.getId());
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
            String query = "UPDATE pompe SET id = ?, idcuve = ? WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, this.id);
            st.setInt (2, this.cuve.getId());
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
            String query = "DELETE FROM pompe WHERE id = ?";
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