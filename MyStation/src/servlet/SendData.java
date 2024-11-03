package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import annexe.Produit;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import user.UserEJBBean;
import user.UserEJBRemote;

public class SendData extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            PrintWriter out = response.getWriter();

            UserEJBRemote u= Util.getUser();

            Produit[] f= (Produit[]) u.getData(new Produit(), "", "", null, "");


            out.println(f);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
