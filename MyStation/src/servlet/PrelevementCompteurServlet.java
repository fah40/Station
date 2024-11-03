package servlet;

import model.Pompe;
import model.Pompiste;
import model.Prelevement;
// import remoteService.StationPersoRemote;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import db.MyConnect;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PrelevementCompteurServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        try {
            Pompe[] allPompe=  Pompe.getAll();
            int userId = (int)session.getAttribute("userId");
            Prelevement[] allPrel=  Prelevement.getAllById(userId);

            request.setAttribute("listPompe", allPompe);
            request.setAttribute("listPrelevement", allPrel);
            request.getRequestDispatcher("compteur.jsp").forward(request, response);
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

            double valeur = Double.parseDouble(request.getParameter("valeur"));
            int idPompe = Integer.parseInt(request.getParameter("idPompe"));
            String date = request.getParameter("date");
            int userId = (int)session.getAttribute("userId");

            Prelevement e = new Prelevement();
            Prelevement ea = null;
            
            // StationPersoRemote ejb= Util.getUser();
            // ejb.insertImediateVenteCse(valeur,1);
            
            e.setId(1);
            e.setPompe(Pompe.getById(idPompe));
            e.setPompiste(Pompiste.getById(userId));
            e.setValeurCompteur(valeur);
            e.setPrelevementAnterieur(ea);
            e.setDate(date);
            
            e.insertProperly(con);
            if (con!=null) {
                con.close();
            }
            doGet(request, response);
            
        } catch (Exception e) {
            out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
