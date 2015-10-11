/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.FleetAssignmentLocal;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author ruicai
 */
@Named(value = "scheduleDevelopmentManagedBean")
@ViewScoped
public class ScheduleDevelopmentManagedBean implements Serializable {

    private Integer flightTimes;
    private String iterationPeriod;
    private List<RouteEntity> connectionsAll;
    @EJB
    private RouteSessionBeanLocal routeSession;
    private RouteEntity routeSelected;
    private Integer yearSelected;
    private List<Integer> years;
    private Date date;
    private List<FlightEntity> flights;
    private List<Integer> serialNo;
    private Integer planningPeiord;
    private String endingDate;
    @EJB
    private FleetAssignmentLocal fl;

    /**
     * Creates a new instance of ScheduleDevelopmentManagedBean
     */
    public ScheduleDevelopmentManagedBean() {
    }

    @PostConstruct
    public void init() {
        System.out.println("init()");

    }

//    public void changeEventListener()
//    {
//        System.err.println("yearSelected: " + yearSelected);
//    }
    public List<FlightEntity> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightEntity> flights) {
        this.flights = flights;
    }

    public Integer getPlanningPeiord() {
        return planningPeiord;
    }

    public void setPlanningPeiord(Integer planningPeiord) {
        this.planningPeiord = planningPeiord;
    }

    public void setFrequencyFinished() throws IOException {
        System.err.println("print date"+date);
        System.out.println("enter setFrequency");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        flights = new ArrayList<FlightEntity>();
        serialNo = new ArrayList<Integer>();
        System.out.println("before enter forloop" + flightTimes);
        for (int i = 0; i < flightTimes; i++) {
            System.out.println("enter forloop");
            FlightEntity flightForRoute = new FlightEntity();
            flightForRoute.setRoute(routeSelected);
            FlightEntity reverseFlight = new FlightEntity();
            reverseFlight.setReverseFlight(flightForRoute);
            reverseFlight.setRoute(routeSelected.getReverseRoute());
            flightForRoute.setReverseFlight(reverseFlight);
            flights.add(flightForRoute);
            System.out.println("enter forloop2");
            serialNo.add(i + 1);
        }
        if (iterationPeriod.equals("Day")) {
            System.out.println("enter setFrequency1");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("flightTimes", flightTimes);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("iterationPeriod", iterationPeriod);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("routeSelected", routeSelected);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("planningPeriod", planningPeiord);
            System.out.println("lalalalalallalallaa  " + routeSelected);

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("yearSelected", yearSelected);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("serialNo", serialNo);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("flights", flights);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("dateOperate", date);

            ec.redirect("planningSetSchedulePerDay.xhtml");
        } else {
            System.out.println("enter setFrequency1");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("flightTimes", flightTimes);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("iterationPeriod", iterationPeriod);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("routeSelected", routeSelected);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("planningPeriod", planningPeiord);
            System.out.println("lalalalalallalallaa  " + routeSelected);

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("yearSelected", yearSelected);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("serialNo", serialNo);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("flights", flights);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("dateOperate", date);
            ec.redirect("planningSetSchedulePerWeek.xhtml");
        }
    }

    public List<Integer> getYears() {
        years = new ArrayList<Integer>();
        years.add(1);
        years.add(2);
        years.add(3);
        return years;
        
//        int year = Calendar.getInstance().get(Calendar.YEAR);
//        year = year + 1;
//        years.add(year);
//        year = year + 1;
//        years.add(year);
//        year = year + 1;
//        years.add(year);
//        return years;
    }

    public void setYears(List<Integer> years) {
        this.years = years;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getYearSelected() {
        return yearSelected;
    }

    public void setYearSelected(Integer yearSelected) {
        this.yearSelected = yearSelected;
    }

    public RouteEntity getRouteSelected() {
        return routeSelected;
    }

    public List<Integer> getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(List<Integer> serialNo) {
        this.serialNo = serialNo;
    }

    public void setRouteSelected(RouteEntity routeSelected) {
        this.routeSelected = routeSelected;
    }

    public List<RouteEntity> getConnectionsAll() {
        System.err.println("testfreq");
        List<RouteEntity> routesAll = routeSession.retrieveAllRoutes();
        connectionsAll = routeSession.filterRoutesToConnections(routesAll);
        System.out.println("enter getconnectionsall");

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("routesRangeList", connectionsAll);

        return connectionsAll;
    }

    public void setConnectionsAll(List<RouteEntity> connectionsAll) {
        this.connectionsAll = connectionsAll;
    }
//     public String getEndingDate(){
//         if(date!=null){
//         Calendar cal = Calendar.getInstance();
//         cal.setTime(date);
//         cal.add(Calendar.YEAR, planningPeiord);
//             System.err.println("getEndingDate() planningPeriod:"+planningPeiord);
//         String endingDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());            
//         return endingDate;}
//         else{
//             return "";
//         }
//        
//    }
    

    public String getEndingDate() {
        System.err.println("changechange");
        if(date!=null)
        {
         Calendar cal = Calendar.getInstance();
         cal.setTime(date);
         cal.add(Calendar.YEAR, planningPeiord);
             System.err.println("getEndingDate() planningPeriod:"+planningPeiord);
          endingDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());            
         
         }
        else
        {
            System.err.println("date is NULL");
        }
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }
    
    public String getMinDate() {
        if(routeSelected!=null){
            
        if(!fl.retreiveDBrecords(routeSelected).isEmpty()){
            
            List<FlightEntity> flights = fl.retreiveDBrecords(routeSelected);
            Date temp = flights.get(0).getDepartureDate();
            Date max = temp;
            for(FlightEntity f: flights){
                if(max.compareTo(f.getDepartureDate())<0)
                    max = f.getDepartureDate();
            }
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(max);
        System.err.println("getMinDate()not emp: printout one year after current date:"+dateFormat.format(cal.getTime()));        
        return dateFormat.format(cal.getTime());
        }
        else{
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        System.err.println("getMinDate(): printout one year after current date:"+dateFormat.format(cal.getTime()));        
        return dateFormat.format(cal.getTime());
        }
        }
        else{
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        System.err.println("getMinDate(): printout one year after current date:"+dateFormat.format(cal.getTime()));        
        return dateFormat.format(cal.getTime());
        }
                //        if (yearSelected != null) {
                //            System.out.println("year:" + yearSelected.toString());
                //            String year1 = String.valueOf(yearSelected);
                //            System.out.println("year1");
                //            System.out.println("1/1/" + year1.substring(2, 3));
                //            return "1/1/" + year1.substring(2, 4);
                //        } else {
                //            System.out.println("year2");
                //
                //            return "";
                //        }

    }

//    public String getMaxDate() {
//        if (yearSelected != null) {
//            System.out.println("year:" + yearSelected.toString());
//            String year1 = String.valueOf(yearSelected);
//            System.out.println("year1");
//            System.out.println("12/31/" + year1.substring(2, 3));
//            return "12/31/" + year1.substring(2, 4);
//        } else {
//            System.out.println("year2");
//
//            return "";
//        }
//
//    }

    public Integer getFlightTimes() {
        return flightTimes;
    }

    public void setFlightTimes(Integer flightTimes) {
        this.flightTimes = flightTimes;
    }

    public String getIterationPeriod() {
        return iterationPeriod;
    }

    public void setIterationPeriod(String iterationPeriod) {
        this.iterationPeriod = iterationPeriod;
    }

}
