package model;

import db.MyConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Mouvement {
    private int id;
    private Compte compte;
    private double debit;
    private double credit;
    private String description;
    private java.sql.Timestamp date;

    public Mouvement () {

    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte cp) {
        this.compte = cp;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
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

    // Get by ID method
    public static Mouvement getById(int id) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        Mouvement instance = null;

        Connection con = MyConnect.getConnection();
        try {
            String query = "SELECT * FROM mouvement WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                instance = new Mouvement();
                instance.setId(rs.getInt("id"));
                instance.setCompte(Compte.getById(rs.getInt("idcompte")));
                instance.setDebit(rs.getDouble("debit"));
                instance.setCredit(rs.getDouble("credit"));
                instance.setDescription(rs.getString("description"));
                instance.setDate(rs.getTimestamp("date"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null && !false) con.close();
        }

        return instance;
    }

    // Get all method
    public static Mouvement[] getAll() throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Mouvement> items = new ArrayList<>();

        try {
            String query = "SELECT * FROM mouvement ORDER BY id ASC";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                Mouvement item = new Mouvement();
                item.setId(rs.getInt("id"));
                item.setCompte(Compte.getById(rs.getInt("idcompte")));
                item.setDebit(rs.getDouble("debit"));
                item.setCredit(rs.getDouble("credit"));
                item.setDescription(rs.getString("description"));
                item.setDate(rs.getTimestamp("date"));
                items.add(item);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null && !false) con.close();
        }

        return items.toArray(new Mouvement[0]);
    }

    // Insert method
    public void insert(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "INSERT INTO mouvement (idcompte, debit, credit, description, date) VALUES (?, ?, ?, ?, ?)";
            st = con.prepareStatement(query);
            System.out.println( "- iooo"+ this.getCompte().getNumCompte());
            st.setInt(1, this.getCompte().getId());
            st.setDouble(2, this.debit);
            st.setDouble(3, this.credit);
            st.setString(4, this.description);
            st.setDate(5, new java.sql.Date(this.date.getTime())); // Convert java.util.Date to java.sql.Date
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

    // Update method
    public void update(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "UPDATE mouvement SET idcompte = ?, debit = ?, credit = ?, description = ?, date = ? WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, this.compte.getId());
            st.setDouble(2, this.debit);
            st.setDouble(3, this.credit);
            st.setString(4, this.description);
            st.setDate(5, new java.sql.Date(this.date.getTime()));
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

    // Delete by ID method
    public static void deleteById(int id) throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        try {
            String query = "DELETE FROM mouvement WHERE id = ?";
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

    public static double getTotalVente (Connection con, int annee) {
        PreparedStatement st = null;
        double totalVente = 0;

        try {
            String query = "select sum(diff) as totalvente from detail_mouvement_compte where num_compte like '7%' and annee = ?";

            st = con.prepareStatement(query);
            st.setInt(1, annee);

            try {
                ResultSet resultSet = st.executeQuery();

                if (resultSet.next()) {
                    totalVente = resultSet.getDouble("totalvente");
                } 

                if (resultSet != null) resultSet.close();
            } catch (Exception e) {
                con.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } 
        }

        return totalVente;
    }

    public static double getTotalCharge (Connection con, int annee) {
        PreparedStatement st = null;
        double totalVente = 0;

        try {
            String query = "select sum(diff) as totalcharge from detail_mouvement_compte where num_compte like '6%' and annee = ?";

            st = con.prepareStatement(query);
            st.setInt(1, annee);

            try {
                ResultSet resultSet = st.executeQuery();

                if (resultSet.next()) {
                    totalVente = resultSet.getDouble("totalcharge");
                } 

                if (resultSet != null) resultSet.close();
            } catch (Exception e) {
                con.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } 
        }

        return totalVente;
    }

    public static double getBenefice (Connection connection, int annee) {
        return ((Mouvement.getTotalVente(connection, annee)) - Mouvement.getTotalCharge(connection, annee));
    }
    public static void main(String[] args) {
        Mouvement mv= new Mouvement();
        
    }
}
