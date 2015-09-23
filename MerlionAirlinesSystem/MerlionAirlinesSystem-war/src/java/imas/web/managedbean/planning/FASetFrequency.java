/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.AircraftEntity;
import imas.planning.sessionbean.AircraftSessionBeanLocal;
import imas.planning.sessionbean.FleetAssignmentLocal;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.PostRemove;

/**
 *
 * @author ruicai
 */
@Named(value = "fASetFrequency")
@ViewScoped
public class FASetFrequency implements Serializable{
    private AircraftEntity aircraft;
    private List<AircraftEntity> aircraftsAll;
    @EJB
    private AircraftSessionBeanLocal aircraftSessionBean;
    @EJB
    private FleetAssignmentLocal fleetassignment;
    private Date planningPeriodStartingDate;
            
    /**
     * Creates a new instance of FASetFrequency
     */
    public FASetFrequency() {
    }
    
     @PostConstruct
    public void init()
    {
         aircraftsAll = aircraftSessionBean.getAircrafts();
         FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("aircraftList", aircraftsAll);

    }

    public List<Date> getAllPlanningPeirod(){
        return fleetassignment.getAllPlanningPeriodStartDateByYear();
    }

    public Date getPlanningPeriodStartingDate() {
        System.err.println("enter getplanningStartDate");
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
