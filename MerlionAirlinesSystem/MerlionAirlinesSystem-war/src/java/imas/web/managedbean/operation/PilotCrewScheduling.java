/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.operation;

import imas.common.entity.PilotEntity;
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
@Named(value = "pilotCrewScheduling")
@ViewScoped
public class PilotCrewScheduling implements Serializable{
    private List<FlightEntity> flightsAll;
    private List<FlightEntity> flightsLeft;
    private List<PilotEntity> pilotsAll;
    @EJB
    private FleetAssignmentLocal fleetAssignment;
    private Date planningPeriodStartingDate;
    @EJB
    private CrewSchedulingSessionBeanLocal crewSchedulingSession;
    @EJB
    private TestFunctionsFAandCSLocal functionsFAandCSLocal;

    /**
     * Creates a new instance of PilotCrewScheduling
     */
    public PilotCrewScheduling() {
    }
    
    @PostConstruct
    public void init()
    {     
        
        System.err.println("enter init");
//         FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("aircraftList", aircraftsAll);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("dateList", getAllPlanningPeirod());
 //       crewSchedulingSession.pilotScheduling(functionsFAandCSLocal.getAllFlights(), functionsFAandCSLocal.getAllPilots());
        
    }
    
    public void pilotAssignment() throws IOException{
        System.err.println("hehehhee");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        
        flightsAll = fleetAssignment.getAllFlightsWithinPlanningPeriod(planningPeriodStartingDate);
        pilotsAll = crewSchedulingSession.retriveAllPilots();
        System.err.println("finishflightsAll");
        List<FlightEntity> flightsL = crewSchedulingSession.pilotScheduling(flightsAll, pilotsAll);       
        System.err.println("out of the optimization");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("flightsLeft", flightsLeft);
        ec.redirect("retrieveDuty.xhtml");

    }

    public List<FlightEntity> getFlightsAll() {
        return flightsAll;
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

    public FleetAssignmentLocal getFleetAssignment() {
        return fleetAssignment;
    }

    public void setFleetAssignment(FleetAssignmentLocal fleetAssignment) {
        this.fleetAssignment = fleetAssignment;
    }
    
    public String getPlanningPeriodName(Date d){
        SimpleDateFormat dateF = new SimpleDateFormat("dd-MM-yyyy");
        String dateStr = dateF.format(d);

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d);
        cal1.add(Calendar.YEAR, 1);
        return dateStr + " to "+ dateF.format(cal1.getTime());
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

    public TestFunctionsFAandCSLocal getFunctionsFAandCSLocal() {
        return functionsFAandCSLocal;
    }

    public void setFunctionsFAandCSLocal(TestFunctionsFAandCSLocal functionsFAandCSLocal) {
        this.functionsFAandCSLocal = functionsFAandCSLocal;
    }
    
}
