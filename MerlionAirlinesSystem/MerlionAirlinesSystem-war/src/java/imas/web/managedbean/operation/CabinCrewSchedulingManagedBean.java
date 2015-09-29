/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.operation;

import imas.common.entity.CabinCrewEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.sessionbean.CabinCrewSchedulingSessionBeanLocal;
import imas.planning.sessionbean.FleetAssignmentLocal;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 *
 * @author ruicai
 */
@Named(value = "cabinCrewSchedulingManagedBean")
@ViewScoped
public class CabinCrewSchedulingManagedBean implements Serializable {

    private List<FlightEntity> flightsAll;
    private List<FlightEntity> flightsLeft;
    private List<CabinCrewEntity> cabinCrewAll;
    @EJB
    private FleetAssignmentLocal fleetAssignment;
    @EJB
    private CabinCrewSchedulingSessionBeanLocal cabinCrewScheduling;
    private Date planningPeriodStartingDate;

    /**
     * Creates a new instance of CabinCrewSchedulingManagedBean
     */
    public CabinCrewSchedulingManagedBean() {
    }

    @PostConstruct
    public void init() {
        System.err.println("enter init");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("dateList", getAllPlanningPeirod());

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

    public List<CabinCrewEntity> getCabinCrewAll() {
        return cabinCrewAll;
    }

    public void setCabinCrewAll(List<CabinCrewEntity> cabinAll) {
        this.cabinCrewAll = cabinAll;
    }

    public Date getPlanningPeriodStartingDate() {
        return planningPeriodStartingDate;
    }

    public void setPlanningPeriodStartingDate(Date planningPeriodStartingDate) {
        this.planningPeriodStartingDate = planningPeriodStartingDate;
    }

    public List<Date> getAllPlanningPeirod() {
        return fleetAssignment.getAllPlanningPeriodStartDateByYear();
    }

    public String getPlanningPeriodName(Date d) {
        SimpleDateFormat dateF = new SimpleDateFormat("dd-MM-yyyy");
        String dateStr = dateF.format(d);

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d);
        cal1.add(Calendar.YEAR, 1);
        return dateStr + " to " + dateF.format(cal1.getTime());
    }

    public void cabinAssignment() throws IOException {
        System.err.println("hehehhee");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        System.err.println("before getAllflights");
        flightsAll = fleetAssignment.getAllFlightsWithinPlanningPeriod(planningPeriodStartingDate);
        System.err.println("before retrieve all cabin ");
        cabinCrewAll = cabinCrewScheduling.retrieveAllCabinCrew();
        
        System.err.println("finishflightsAll");
        cabinCrewScheduling.CabinScheduling(flightsAll, cabinCrewAll);
        System.err.println("out of the optimization");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("flightsLeft", flightsLeft);
        ec.redirect("retrieveDuty.xhtml");

    }
}
