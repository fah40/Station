package servlet;

import model.Pompiste;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

    

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        
        try {
            String username = request.getParameter("name");
            String pass = request.getParameter("password");
            if(username.isEmpty() || pass.isEmpty()){
                response.sendRedirect("index.jsp");
            }
            if (model.Pompiste.login(username, pass) != null) {
                Pompiste e = Pompiste.login(username, pass);

                session.setAttribute("userId", e.getId());
                session.setAttribute("nom", e.getNom());
                session.setAttribute("userName", e.getUsername());
                session.setAttribute("mdp", e.getMdp());
                session.setAttribute("idPompe", Integer.parseInt(request.getParameter("idPompe")));

                response.sendRedirect("home.jsp");
            } else {
                response.sendRedirect("index.jsp");
            }
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }
}
