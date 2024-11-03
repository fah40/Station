package model;
import db.MyConnect;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
public class Prelevement {
    private int id;
    private Pompe pompe;
    private Pompiste pompiste;
    private Prelevement prelevementAnterieur;
    private double valeurCompteur;
    private java.sql.Timestamp date;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pompe getPompe() {
        return pompe;
    }

    public void setPompe(Pompe pompe) {
        this.pompe = pompe;
    }

    public Pompiste getPompiste() {
        return pompiste;
    }

    public void setPompiste(Pompiste pompiste) {
        this.pompiste = pompiste;
    }

    public Prelevement getPrelevementAnterieur() {
        return prelevementAnterieur;
    }

    public void setPrelevementAnterieur(Prelevement prelevement) {
        this.prelevementAnterieur = prelevement;
    }

    public double getValeurCompteur() {
        return valeurCompteur;
    }

    public void setValeurCompteur(double valeur_compteur) {
        this.valeurCompteur = valeur_compteur;
    }

    public java.sql.Timestamp getDate() {
        return date;
    }

    public void setDate(java.sql.Timestamp date) {
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

    public static Prelevement getById(int id) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        Prelevement instance = null;

        Connection con = MyConnect.getConnection();
        try {
            String query = "SELECT * FROM prelevement WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                instance = new Prelevement();
                instance.setId(rs.getInt("id"));
                instance.setPompe(Pompe.getById(rs.getInt("idpompe")));
                instance.setPompiste(Pompiste.getById(rs.getInt("idpompiste")));
                instance.setPrelevementAnterieur(Prelevement.getById(rs.getInt("idprelevementanterieur")));
                instance.setValeurCompteur(rs.getDouble("valeur_compteur"));
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
    public static Prelevement[] getAll() throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Prelevement> items = new ArrayList<>();

        try {
            String query = "SELECT * FROM prelevement order by id asc ";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                Prelevement item = new Prelevement();
                item.setId(rs.getInt("id"));
                item.setPompe(Pompe.getById(rs.getInt("idpompe")));
                item.setPompiste(Pompiste.getById(rs.getInt("idpompiste")));
                item.setPrelevementAnterieur(Prelevement.getById(rs.getInt("idprelevementanterieur")));
                item.setValeurCompteur(rs.getDouble("valeur_compteur"));
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

        return items.toArray(new Prelevement[0]);
    }
    public static Prelevement[] getAllById(int id) throws Exception {
        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Prelevement> items = new ArrayList<>();

        try {
            String query = "SELECT * FROM prelevement where idpompiste = ? order by id asc ";
            st = con.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            while (rs.next()) {
                Prelevement item = new Prelevement();
                item.setId(rs.getInt("id"));
                item.setPompe(Pompe.getById(rs.getInt("idpompe")));
                item.setPompiste(Pompiste.getById(rs.getInt("idpompiste")));
                item.setPrelevementAnterieur(Prelevement.getById(rs.getInt("idprelevementanterieur")));
                item.setValeurCompteur(rs.getDouble("valeur_compteur"));
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

        return items.toArray(new Prelevement[0]);
    }
    public void insert(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "INSERT INTO prelevement (idpompe, idpompiste, idprelevementanterieur, valeur_compteur, date) VALUES (?, ?, ?, ?, ?)";
            st = con.prepareStatement(query);
            st.setInt(1, this.pompe.getId());
            st.setInt(2, this.pompiste.getId());
            if (this.prelevementAnterieur != null) {
                st.setInt(3, this.prelevementAnterieur.getId());
            } else {
                st.setNull(3, java.sql.Types.INTEGER);
            }
            st.setDouble(4, this.valeurCompteur);
            st.setTimestamp(5, this.date);
            try {
                st.executeUpdate();
                con.commit(); // Commit explicite après succès
            } catch (Exception e) {
                con.rollback(); // Rollback explicite en cas d'erreur
                throw new Exception("Failed to insert record", e);
            }
        } finally {
            if (st != null) st.close();
        }
    }
    
    public void update(Connection con) throws Exception {
        PreparedStatement st = null;
        try {
            String query = "UPDATE prelevement SET id = ?, idpompe = ?, idpompiste = ?, idprelevementanterieur = ?, valeur_compteur = ?, date = ? WHERE id = ?";
            st = con.prepareStatement(query);
            st.setInt(1, this.id);
            st.setInt (2, this.pompe.getId());
            st.setInt (3, this.pompiste.getId());
            st.setInt (4, this.prelevementAnterieur.getId());
            st.setDouble(5, this.valeurCompteur);
            st.setTimestamp(6, this.date);
            st.setInt(7, this.getId());
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
            String query = "DELETE FROM prelevement WHERE id = ?";
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

    public static boolean getIsEntry (Connection con, int idPompe) {
        boolean rep = false;

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String query = "SELECT (count(*) % 2) = 0 as rep FROM prelevement where idpompe = ?";
            st = con.prepareStatement(query);
            st.setInt(1, idPompe);
            rs = st.executeQuery();

            if (rs.next()) {
                rep = rs.getBoolean("rep");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return rep;
    }

    public Prelevement getPreviousOne (Connection con) {
        Prelevement prelevement = null;

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String query = "SELECT * FROM prelevement where idpompe = ? order by date desc limit 1";
            st = con.prepareStatement(query);
            st.setInt(1, this.pompe.getId());
            rs = st.executeQuery();

            if (rs.next()) {
                int idPrevious = rs.getInt("id");

                prelevement = Prelevement.getById(idPrevious);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return prelevement;
    }

    public static double[] getDernierCompteur (int idPompe) {

        Connection con = MyConnect.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        double[] res=new double[3];
        Vector<Double> vals= new Vector<>();
        
        try {
            String query = "SELECT valeur_compteur,prix_unitaire FROM Prelevement_produit_cuve where idpompe = ? order by date desc limit 2";
            st = con.prepareStatement(query);
            st.setInt(1, idPompe);
            rs = st.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getDouble("valeur_compteur"));
                vals.add(rs.getDouble("valeur_compteur"));
                vals.add(rs.getDouble("prix_unitaire"));
            }
            res[0] = (double)vals.get(0) - (double)vals.get(2);
            res[1] = (double)vals.get(1)*res[0];
            res[2] = (double)vals.get(1);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return res;
    } 

    public void insertProperly (Connection connection) {
        boolean isEntry = Prelevement.getIsEntry(connection, this.pompe.getId());

        if (isEntry) {
            try {
                this.setPrelevementAnterieur(this.getPreviousOne(connection));
                this.insert(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.setPrelevementAnterieur(this.getPreviousOne(connection));

            double quantity = this.valeurCompteur - this.prelevementAnterieur.valeurCompteur;
            Sortie sortie = new Sortie(this.getPompe().getCuve().getId(), quantity, this.date);            
        
            try {
                sortie.insert(connection);
                this.insert(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

// By Jos 