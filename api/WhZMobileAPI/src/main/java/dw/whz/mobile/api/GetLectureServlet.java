/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dw.whz.mobile.api;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mindia
 */
@WebServlet(name = "GetLecture", urlPatterns = {"/get-nearby-places"})
public class GetLectureServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            NearByPlace byPlace = new NearByPlace();
            byPlace.setName("Cafe something");
            ArrayList<NearByPlace> al = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                al.add(byPlace);
            }
            out.print(new Gson().toJson(al));

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
