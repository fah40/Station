package model;

import java.sql.*;
import java.util.*;
import db.MyConnect;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Creance {
    private int id;
    private Client client;  // Relation avec la table client
    private double montant;
    private Timestamp date;
    private Timestamp datepa;

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Timestamp getDatepa() {
        return datepa;
    }

    public void setDatepa(Timestamp datepa) {
        this.datepa = datepa;
    }

    public void setDatepa(String date) {
        LocalDate localDate = LocalDate.parse(date);
        // Convertir LocalDate en LocalDateTime en ajoutant minuit comme heure
        LocalDateTime localDateTime = localDate.atStartOfDay();
        // Convertir LocalDateTime en Timestamp
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        this.date = timestamp;
    }

    // Méthode pour obtenir une créance par ID
    public static Creance getById(int id) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        Creance instance = null;

        Connection con = MyConnect.getConnection();
        try {
            String query = "SELECT * FROM creance WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                instance = new Creance();
                instance.setId(rs.getInt("id"));
                instance.setClient(Client.getById(rs.getInt("idClient")));
                instance.setMontant(rs.getDouble("montant"));
                instance.setDate(rs.getTimestamp("date"));
                instance.setDatepa(rs.getTimestamp("datepa"));
            }
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null && !con.isClosed()) con.close();
        }

        return instance;
    }

    // Méthode pour obtenir toutes les créances
    public static Creance[] getAll() throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Creance> items = new ArrayList<>();

        try {
            String query = "SELECT * FROM creance ORDER BY id ASC";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                Creance item = new Creance();
                item.setId(rs.getInt("id"));
                item.setClient(Client.getById(rs.getInt("idClient")));
                item.setMontant(rs.getDouble("montant"));
                item.setDate(rs.getTimestamp("date"));
                item.setDatepa(rs.getTimestamp("datepa"));
                items.add(item);
            }
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null && !con.isClosed()) con.close();
        }

        return items.toArray(new Creance[0]);
    }

    // Méthode pour insérer une nouvelle créance
    public void insert(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "INSERT INTO creance (idClient, montant, datepa) VALUES (?, ?, ?)";
            st = con.prepareStatement(query);
            st.setInt(1, this.client.getId());
            st.setDouble(2, this.montant);
            st.setTimestamp(3, this.datepa);

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

    // Méthode pour mettre à jour une créance
    public void update(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "UPDATE creance SET idClient = ?, montant = ?, datepa = ? WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, this.client.getId());
            st.setDouble(2, this.montant);
            st.setTimestamp(3, this.datepa);
            st.setInt(4, this.id);

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

    // Méthode pour supprimer une créance par ID
    public static void deleteById(int id) throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        try {
            String query = "DELETE FROM creance WHERE id = ?";
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
            if (con != null && !con.isClosed()) con.close();
        }
    }
}
