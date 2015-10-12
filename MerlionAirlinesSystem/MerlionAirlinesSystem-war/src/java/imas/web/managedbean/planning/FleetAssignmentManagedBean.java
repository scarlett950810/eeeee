/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.sessionbean.AircraftSessionBeanLocal;
import imas.planning.sessionbean.CrewSchedulingSessionBeanLocal;
import imas.planning.sessionbean.FleetAssignmentLocal;
import imas.planning.sessionbean.TestFunctionsFAandCSLocal;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author ruicai
 */
@Named(value = "fleetAssignmentManagedBean")
@ViewScoped
public class FleetAssignmentManagedBean implements Serializable{
    private AircraftEntity aircraft;
    private List<AircraftEntity> aircraftsAll;
    private List<FlightEntity> flightsAll;
    private List<FlightEntity> flightsLeft;
    @EJB
    private AircraftSessionBeanLocal aircraftSessionBean;
    @EJB
    private FleetAssignmentLocal fleetAssignment;
    private Date planningPeriodStartingDate;
    @EJB
    private CrewSchedulingSessionBeanLocal crewSchedulingSession;
    @EJB
    private TestFunctionsFAandCSLocal functionsFAandCSLocal;
    private Integer numOfUnassignedFlights;
    private Boolean appearOrnot = true; 
    private String color = "color: indianred";
    /**
     * Creates a new instance of FASetFrequency
     */
    public FleetAssignmentManagedBean() {

    }
    
     @PostConstruct
    public void init()
    {     
//        numOfUnassignedFlights = fleetAssignment.getAllFlights().size();
//        if(numOfUnassignedFlights==0){
//            appearOrnot = false;
//            color = "#999999";
//        }
//        System.err.println("enter init");
////         FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("aircraftList", aircraftsAll);
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("dateList", getAllPlanningPeirod());
 //       crewSchedulingSession.pilotScheduling(functionsFAandCSLocal.getAllFlights(), functionsFAandCSLocal.getAllPilots());
        
    }
    public void fleetAssignment() throws IOException{
        System.err.println("hehehhee");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        aircraftsAll = aircraftSessionBean.getAircrafts();  
      //  System.err.println("planningPeriodStartingDate"+planningPeriodStartingDate.toString());
        flightsAll = fleetAssignment.getAllFlights();
        System.err.println("finishflightsAll");
        flightsLeft = fleetAssignment.fleetAssignment(flightsAll, aircraftsAll);
        System.err.println("outof the optimization");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("flightsLeft", flightsLeft);
 //       fleetAssignment.deleteUnassginedFlights(flightsLeft);
        ec.redirect("../operation/viewFlightSchedule.xhtml");

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<FlightEntity> getFlightsAll() {
        return flightsAll;
    }

    public Boolean getAppearOrnot() {
        return appearOrnot;
    }

    public void setAppearOrnot(Boolean appearOrnot) {
        this.appearOrnot = appearOrnot;
    }

    public void setFlightsAll(List<FlightEntity> flightsAll) {
        this.flightsAll = flightsAll;
    }

    public List<FlightEntity> getFlightsLeft() {
        return flightsLeft;
    }

    public void setFlightsLeft(List<FlightEntity> flightsLeft) {
        this.flightsLeft = flightsLeft;
    }

    public Integer getNumOfUnassignedFlights() {
        return numOfUnassignedFlights;
    }

    public void setNumOfUnassignedFlights(Integer numOfUnassignedFlights) {
        this.numOfUnassignedFlights = numOfUnassignedFlights;
    }

    public List<Date> getAllPlanningPeirod(){
        return fleetAssignment.getAllPlanningPeriodStartDateByYear();
    }

    public Date getPlanningPeriodStartingDate() {
        System.err.println("enter...."+planningPeriodStartingDate);
        return planningPeriodStartingDate;
    }

    public void setPlanningPeriodStartingDate(Date planningPeriodStartingDate) {
        this.planningPeriodStartingDate = planningPeriodStartingDate;
    }
    
    public String getPlanningPeriodName(Date d){
        SimpleDateFormat dateF = new SimpleDateFormat("dd-MM-yyyy");
        String dateStr = dateF.format(d);

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d);
        cal1.add(Calendar.YEAR, 1);
        return dateStr + " to "+ dateF.format(cal1.getTime());
    }
    
    
    public AircraftEntity getAircraft() {
        return aircraft;
    }

    public void setAircraft(AircraftEntity aircraft) {
        this.aircraft = aircraft;
    }

    public List<AircraftEntity> getAircraftsAll() {
        return aircraftsAll;
    }

    public void setAircraftsAll(List<AircraftEntity> aircraftsAll) {
        this.aircraftsAll = aircraftsAll;
    }
    
    
    
}
