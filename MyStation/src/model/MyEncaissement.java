package model;

import db.MyConnect;
import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MyEncaissement {
    private int id;
    private double montant;
    private double paye;
    private double quantite;
    private Cuve cuve;
    private Compte compte;
    private java.sql.Timestamp date;

    // Getters and Setters
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

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte cp) {
        this.compte = cp;
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

    // Methods for database operations
    public static MyEncaissement getById(int id) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        MyEncaissement instance = null;

        Connection con = MyConnect.getConnection();
        try {
            String query = "SELECT * FROM encaissement WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                instance = new MyEncaissement();
                instance.setId(rs.getInt("id"));
                instance.setMontant(rs.getDouble("montant"));
                instance.setPaye(rs.getDouble("paye"));
                instance.setCompte(Compte.getById(rs.getInt("idcompte")));
                instance.setDate(rs.getTimestamp("date"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null)
                rs.close();
            if (st != null)
                st.close();
            if (con != null)
                con.close();
        }

        return instance;
    }

    public static MyEncaissement[] getAll() throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<MyEncaissement> items = new ArrayList<>();

        try {
            String query = "SELECT * FROM encaissement ORDER BY id ASC";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                MyEncaissement item = new MyEncaissement();
                item.setId(rs.getInt("id"));
                item.setMontant(rs.getDouble("montant"));
                item.setPaye(rs.getDouble("paye"));
                item.setCompte(Compte.getById(rs.getInt("idcompte")));
                item.setDate(rs.getTimestamp("date"));
                items.add(item);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null)
                rs.close();
            if (st != null)
                st.close();
            if (con != null)
                con.close();
        }

        return items.toArray(new MyEncaissement[0]);
    }

    public void insert(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "INSERT INTO encaissement (montant, paye, quantite, idcompte, date) VALUES (?, ?, ?, ?, ?)";
            st = con.prepareStatement(query);
            st.setDouble(1, this.montant);
            st.setDouble(2, this.paye);
            st.setDouble(3, this.quantite);
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
            if (st != null)
                st.close();
        }
    }

    public void insertProperly(Connection connection) {

        try {
            this.insert(connection);
            double quantiteRestante = this.getCuve().getQuantite() - this.quantite;
            
            if (quantiteRestante < 0) {
                throw new Exception("Quantité insuffisante!!!");
            }
            this.getCuve().setQuantite(quantiteRestante);
            this.getCuve().update(connection);
            
            if (this.paye > 0) {
                Mouvement du = new Mouvement();
                du.setCompte(this.getCompte());
                du.setDebit(this.montant);
                du.setCredit(0);
                du.setDescription("Facture de vente de " + this.montant);
                du.setDate(this.date);
                
                System.out.println(this.getCompte());
                du.insert(connection);

                Mouvement payement = new Mouvement();
                payement.setCompte(this.getCompte());
                payement.setDebit(0);
                payement.setCredit(this.paye);
                payement.setDescription("Facture de réglage de " + this.paye);
                payement.setDate(this.date);

                payement.insert(connection);
            } else {
                Mouvement du = new Mouvement();
                du.setCompte(this.getCompte());
                du.setDebit(this.montant);
                du.setCredit(0);
                du.setDescription("Facture de vente de " + this.montant);
                du.setDate(this.date);
            }

        } catch (Exception e) {
            try {
                connection.rollback();
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void update(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "UPDATE encaissement SET montant = ?, paye = ?, idcompte = ?, date = ? WHERE id = ?";
            st = con.prepareStatement(query);
            st.setDouble(1, this.montant);
            st.setDouble(2, this.paye);
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
            if (st != null)
                st.close();
        }
    }

    public static void deleteById(int id) throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        try {
            String query = "DELETE FROM encaissement WHERE id = ?";
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
            if (st != null)
                st.close();
            if (con != null)
                con.close();
        }
    }
    public static void main(String[] args) throws Exception {
        Cuve cv= Cuve.getById(1);
        Compte cp= Compte.getByNum(7);
        double montant = 100000;
        double paye = 90000;
        double quantite = Prelevement.getDernierCompteur(1)[1];
        
        MyEncaissement pre= new MyEncaissement();
        pre.deleteById(1);
        pre.setMontant(montant);
        pre.setCompte(cp);
        pre.setCuve(cv);
        pre.setPaye(paye);
        pre.setDate("2024-10-14");

        Connection con= MyConnect.getConnection();

        pre.insertProperly(con);
    }
}
