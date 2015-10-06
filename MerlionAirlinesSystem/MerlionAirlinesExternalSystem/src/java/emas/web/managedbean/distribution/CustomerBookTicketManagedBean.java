/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.distribution;

import java.io.InputStream;
import java.util.HashMap;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author Howard
 */
@ManagedBean
@SessionScoped
public class CustomerBookTicketManagedBean {

    @Resource(name = "merlionAirlineDataSource")
    private DataSource merlionAirlineDataSource;

    public CustomerBookTicketManagedBean() {
    }

    public void confirm() {
        generateItinerary();
        System.out.print("finished");
    }

    private void generateItinerary() {
        try {
            System.out.print("here we go!");
            HttpServletResponse httpServletResponse = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            System.out.print("1");
            httpServletResponse.setContentType("application/pdf");
            System.out.print("2");
            InputStream reportStream
                    = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperReports/FlightItinerary.jasper");
            System.out.print("3");
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            System.out.print("4");
            HashMap parameters = new HashMap();
            System.out.print("5");
            parameters.put("IMAGEPATH", "http://localhost:8081/EventBookingSystem-war/resources/img/NEW_LOGO.png");
            parameters.put("STAFFNAME", "Howard");
            System.out.print("6");
            JasperRunManager.runReportToPdfStream(reportStream, outputStream,
                    parameters, merlionAirlineDataSource.getConnection());
            System.out.print("7");
            outputStream.flush();
            System.out.print("8");
            outputStream.close();
        }catch (JRException jrex){
            System.out.println("********** Jasperreport Exception");
            jrex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
