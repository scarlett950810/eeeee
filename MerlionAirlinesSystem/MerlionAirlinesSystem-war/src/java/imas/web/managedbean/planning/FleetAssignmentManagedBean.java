/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.sessionbean.AircraftSessionBeanLocal;
import imas.planning.sessionbean.FleetAssignmentLocal;
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
            
    /**
     * Creates a new instance of FASetFrequency
     */
    public FleetAssignmentManagedBean() {
    }
    
     @PostConstruct
    public void init()
    {     
//         FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("aircraftList", aircraftsAll);

    }
    public void fleetAssignment() throws IOException{
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        aircraftsAll = aircraftSessionBean.getAircrafts();  
        flightsAll = fleetAssignment.getAllFlightsWithinPlanningPeriod(planningPeriodStartingDate);
        flightsLeft = fleetAssignment.fleetAssignment(flightsAll, aircraftsAll);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("flightsLeft", flightsLeft);
        ec.redirect("planningFleetAssignmentDisplay.xhtml");

    }

    public List<Date> getAllPlanningPeirod(){
        return fleetAssignment.getAllPlanningPeriodStartDateByYear();
    }

    public Date getPlanningPeriodStartingDate() {
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
