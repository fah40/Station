package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnect {
    static String user = "postgres";
    static String pass = "mazoto";
    static String base = "station";
    static String type = "postgres";
    public static Connection getConnection() {
        Connection conex;
        try {

            if (type == "oracle") {
                conex = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL", user, pass);
            } else if (type == "postgres") {
                Class.forName("org.postgresql.Driver");
                conex = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + base, user, pass);
            } else if (type == "mysql") {
                conex = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + base, user, pass);
            } else {
                Exception or = new Exception("Verifer la Connection");
                throw or;
            }
            conex.setAutoCommit(false);
            return conex;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }
}
