package servlet;

import model.Cuve;
import model.Pompiste;
import model.PrelevementCuve;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import db.MyConnect;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PrelevementCuveServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        try {
            Cuve[] allCuves=  Cuve.getAll();
            PrelevementCuve[] allPrel=  PrelevementCuve.getAll();

            request.setAttribute("listCuve", allCuves);
            request.setAttribute("listPlCuve", allPrel);
            request.getRequestDispatcher("cuve.jsp").forward(request, response);
        } catch (Exception e) {
            out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        Connection con = MyConnect.getConnection();
        
        try {
            if(request.getParameter("valeur").isEmpty() || request.getParameter("date").isEmpty()){
                doGet(request, response);
            }

            String valeurString = request.getParameter("valeur");
            double valeur = Double.parseDouble(valeurString);
            
            int idCuve = Integer.parseInt(request.getParameter("idCuve"));
            String date = request.getParameter("date");
            int userId = (int)session.getAttribute("userId");

            PrelevementCuve e = new PrelevementCuve();
            
            e.setCuve(Cuve.getById(idCuve));
            e.setPompiste(Pompiste.getById(userId));
            e.setValeurCuve(valeur);
            e.setDate(date);
            
            e.insert(con);
            if (con!=null) {
                con.close();
            }
            doGet(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            out.println(e.getMessage());
        }
    }
}
