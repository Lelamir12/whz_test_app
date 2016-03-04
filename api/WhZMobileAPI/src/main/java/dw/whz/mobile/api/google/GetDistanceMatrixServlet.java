/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dw.whz.mobile.api.google;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mindia
 */
@WebServlet(name = "GetDistanceMatrixServlet", urlPatterns = {"/get-distance-matrix"})
public class GetDistanceMatrixServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        String origin = request.getParameter("origin");
        String destinations = request.getParameter("destination");

        try {
            HttpResponse<String> body = Unirest.post("https://maps.googleapis.com/maps/api/distancematrix/json")
                    .queryString("origins", origin)
                    .queryString("destinations", destinations)
                    .asString();
            try (PrintWriter out = response.getWriter()) {
                out.printf(body.getBody());
            }
        } catch (UnirestException ex) {
            Logger.getLogger(GetDistanceMatrixServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
