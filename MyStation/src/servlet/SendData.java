package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import remoteService.StationPersoRemote;
import remoteService.StationPersoRemote;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import model.Produit;

public class SendData extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Produit[] allProd= Produit.getAll();
            Gson gson = new Gson();
            String jsonProduits = gson.toJson(produits); 
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
