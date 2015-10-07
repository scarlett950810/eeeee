/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.departure;

import imas.departure.sessionbean.DelayManagementSessionBeanLocal;
import imas.departure.sessionbean.TimeManagementSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Lei
 */
@Named(value = "timeManagementManagedBean")
@ViewScoped
public class TimeManagementManagedBean implements Serializable {
    
    @EJB
    TimeManagementSessionBeanLocal timeManagementSessionBean;
    @EJB
    DelayManagementSessionBeanLocal delayManagementSessionBean;
    private String base = "SGC";
    private List<FlightEntity> comingFlights;
    private List<FlightEntity> flyingFlights;
    private List<FlightEntity> unprocessFlights;
    private FlightEntity flight;
//    private Date departureTime;
//    private Date arriveTime;
//    private Date actualDepartureTime;
//    private Date actualArriveTime;
    private Date checkInStartTime;
    private Date checkInCloseTime;
    private Date boardingStartTime;
    private Date boradingCloseTime;
    private boolean display = false;
    
    @PostConstruct
    public void init() {
        fetchFlights();
    }
    
    public void fetchFlights() {
        comingFlights = timeManagementSessionBean.fetchComingFlights(base);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("allFlights", comingFlights);
    }
    
    public void onFlightChange() {
        if (flight != null) {
//            departureTime = flight.getDepartureDate();
//            arriveTime = flight.getArrivalDate();
//            actualDepartureTime = flight.getActualArrivalDate();
//            actualArriveTime = flight.getActualArrivalDate();
            checkInStartTime = new Date(flight.getDepartureDate().getTime() - (1000 * 60 * 60 * 3));
            checkInCloseTime = new Date(flight.getDepartureDate().getTime() - (1000 * 60 * 30));
            boardingStartTime = new Date(flight.getDepartureDate().getTime() - (1000 * 60 * 60));
            boradingCloseTime = new Date(flight.getDepartureDate().getTime() - (1000 * 60 * 5));
            
            display = true;
            System.out.print(flight);
            
        }
    }
    
    public void updateActualDepartureTime() {
        int result = timeManagementSessionBean.updateActualDepartureTime(flight);
        if (result == 1) {
            
            FacesMessage msg = new FacesMessage("Reminder", "Actual departure time for " + flight.getFlightNo() + " has been udated");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Sorry", "Actual departure time for " + flight.getFlightNo() + " already exists");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void updateActualArriveTime() {
        if (flight.getActualDepartureDate() == null) {
            FacesMessage msg = new FacesMessage("Sorry", flight.getFlightNo() + " doesn't have actual departure time");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            
            int result = timeManagementSessionBean.updateActualArriveTime(flight);
            if (result == 1) {
                delayManagementSessionBean.fetchDelayFlights(flight);
                FacesMessage msg = new FacesMessage("Reminder", "Actual arrive time for " + flight.getFlightNo() + " has been udated");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                FacesMessage msg = new FacesMessage("Sorry", "Actual arrive time for " + flight.getFlightNo() + " already exists");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
        
    }
    
    public Date getCheckInStartTime() {
        return checkInStartTime;
    }
    
    public void setCheckInStartTime(Date checkInStartTime) {
        this.checkInStartTime = checkInStartTime;
    }
    
    public Date getCheckInCloseTime() {
        return checkInCloseTime;
    }
    
    public void setCheckInCloseTime(Date checkInCloseTime) {
        this.checkInCloseTime = checkInCloseTime;
    }
    
    public Date getBoardingStartTime() {
        return boardingStartTime;
    }
    
    public void setBoardingStartTime(Date boardingStartTime) {
        this.boardingStartTime = boardingStartTime;
    }
    
    public Date getBoradingCloseTime() {
        return boradingCloseTime;
    }
    
    public void setBoradingCloseTime(Date boradingCloseTime) {
        this.boradingCloseTime = boradingCloseTime;
    }
    
    public boolean isDisplay() {
        return display;
    }
    
    public void setDisplay(boolean display) {
        this.display = display;
    }
    
    public String getBase() {
        return base;
    }
    
    public void setBase(String base) {
        this.base = base;
    }
    
    public List<FlightEntity> getComingFlights() {
        return comingFlights;
    }
    
    public void setComingFlights(List<FlightEntity> comingFlights) {
        this.comingFlights = comingFlights;
    }
    
    public List<FlightEntity> getFlyingFlights() {
        return flyingFlights;
    }
    
    public void setFlyingFlights(List<FlightEntity> flyingFlights) {
        this.flyingFlights = flyingFlights;
    }
    
    public List<FlightEntity> getUnprocessFlights() {
        return unprocessFlights;
    }
    
    public void setUnprocessFlights(List<FlightEntity> unprocessFlights) {
        this.unprocessFlights = unprocessFlights;
    }
    
    public FlightEntity getFlight() {
        return flight;
    }
    
    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }
    
}
