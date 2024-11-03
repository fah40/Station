package servlet;

import model.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Integers;

import db.MyConnect;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Save extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        try {
            double[] rep= Prelevement.getDernierCompteur((int)session.getAttribute("idPompe"));
            System.out.println(rep[0]+" - "+ rep[1] +" - " + rep[2]);
            request.setAttribute("compteur", rep[0]);
            request.setAttribute("totalPrix", rep[1]);
            request.setAttribute("pu", rep[2]);

            int userId = (int)session.getAttribute("userId");
            
            request.getRequestDispatcher("encaissement.jsp").forward(request, response);
        } catch (Exception e) {
            out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String mode= request.getParameter("mode"); 
        Connection con = MyConnect.getConnection();
        HttpSession session = request.getSession();

        try {
            if (mode.equals("f")) {
                if(request.getParameter("paye").isEmpty() && request.getParameter("date").isEmpty()){
                    doGet(request, response);
                }
                double quantite = Double.parseDouble(request.getParameter("compteur"));
                double montant = Double.parseDouble(request.getParameter("montant"));
                double paye = Double.parseDouble(request.getParameter("paye"));
                Cuve cuve= Cuve.getById((int)session.getAttribute("idPompe"));
                Compte compte = Compte.getByNum(7);
                String date = request.getParameter("date");
                System.out.println(compte.getNumCompte());
                MyEncaissement en = new MyEncaissement();
                
                en.setMontant(montant);
                en.setPaye(paye);
                en.setQuantite(quantite);
                en.setCuve(cuve);
                en.setCompte(compte);
                en.setDate(date);
    
                if (paye - montant < 0) {
                    double rest= montant-paye;
                    Client[] allCli= Client.getAll();
                    request.setAttribute("creance", rest);
                    request.setAttribute("allClient", allCli);
                }
                en.insertProperly(con);
                if (con != null){
                    con.close();
                }

                doGet(request, response);
            }else{
                if(request.getParameter("paye").isEmpty() || request.getParameter("date").isEmpty()){
                    doGet(request, response);
                }
                Client cl= Client.getById(Integer.parseInt(request.getParameter("client")));
                double montant = Double.parseDouble(request.getParameter("rest"));
                String date = request.getParameter("date");

                Creance cre= new Creance();
                cre.setClient(cl);
                cre.setMontant(montant);
                cre.setDatepa(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println(e.getMessage());
        }
          
    }
}
