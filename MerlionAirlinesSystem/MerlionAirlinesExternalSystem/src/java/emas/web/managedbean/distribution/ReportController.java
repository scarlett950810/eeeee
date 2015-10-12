/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.distribution;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author Howard
 */
@WebServlet(name = "ReportController", urlPatterns = {"/ReportController", "/ReportController?*"})
public class ReportController extends HttpServlet {

    @Resource(name = "merlionAirlineDataSource")
    private DataSource merlionAirlineDataSource;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        generateItinerary(request, response);

    }

    private void generateItinerary(HttpServletRequest request, HttpServletResponse response) {
        try {
            String userName = request.getParameter("User");
            System.out.print("here we go!" + userName);
            InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream("/jasperReports/FlightItinerary.jasper");
            System.out.print("1");
            response.setContentType("application/pdf");
            System.out.print("2");
            ServletOutputStream outputStream = response.getOutputStream();
            System.out.print(outputStream);
            HashMap parameters = new HashMap();
            System.out.print("5");
            parameters.put("IMAGEPATH", "https://localhost:8181/MerlionAirlinesExternalSystem/resources/img/NEW_LOGO.png");
            parameters.put("STAFFNAME", userName);
            System.out.print("6");
            JasperRunManager.runReportToPdfStream(reportStream, outputStream,
                    parameters, merlionAirlineDataSource.getConnection());
            System.out.print("7");
            outputStream.flush();
            System.out.print("8");

        } catch (JRException jrex) {
            System.out.println("********** Jasperreport Exception");
            jrex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
