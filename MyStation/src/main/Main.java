package main;

import db.MyConnect;
import java.sql.Connection;
import model.Mouvement;
import model.Prelevement;

public class Main {
    public static void main (String[] args) {
        try {
            Connection connection = MyConnect.getConnection();

            double totalVente = Mouvement.getTotalVente(connection, 2024);
            double totalCharge = Mouvement.getTotalCharge(connection, 2024);

            System.out.println("Vente: " + totalVente);
            System.out.println("Charge: " + totalCharge);

            System.out.println("IsEntry:" + Prelevement.getIsEntry(connection, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
