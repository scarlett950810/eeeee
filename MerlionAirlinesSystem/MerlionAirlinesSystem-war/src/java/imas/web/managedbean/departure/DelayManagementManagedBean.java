/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.departure;

import imas.departure.sessionbean.DelayManagementSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Lei
 */
@Named(value = "delayManagementManagedBean")
@ViewScoped
public class DelayManagementManagedBean implements Serializable {

    private String base = "SGC";
    private List<FlightEntity> delayFlights;
    private FlightEntity flight;
    @EJB
    DelayManagementSessionBeanLocal delayManagementSessionBean;
    
    
    
    @PostConstruct
    public void init() {
        fetchFlights();
    }

    public void fetchFlights() {
        delayFlights =delayManagementSessionBean.fetchFlights(base);
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public List<FlightEntity> getDelayFlights() {
        return delayFlights;
    }

    public void setDelayFlights(List<FlightEntity> delayFlights) {
        this.delayFlights = delayFlights;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }
    
    
    
    
}
