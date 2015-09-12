/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.inventory.sessionbean.inventoryRevenueManagementSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Howard
 */
@Named(value = "inventoryRevenueManagementManagedBean")
@ViewScoped
public class inventoryRevenueManagementManagedBean implements Serializable{
    @EJB
    private inventoryRevenueManagementSessionBeanLocal inventoryRevenueManagementSessionBean;

    
    
    private List<FlightEntity> flightList;
    private List<FlightEntity> fliteredFlight;

    
    public inventoryRevenueManagementManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        fetchFlights();
    }
    
    public void fetchFlights(){
        flightList = inventoryRevenueManagementSessionBean.fetchFlight();
    }

    public List<FlightEntity> getFlightList() {
        return flightList;
    }

    public void setFlightList(List<FlightEntity> flightList) {
        this.flightList = flightList;
    }

    public List<FlightEntity> getFliteredFlight() {
        return fliteredFlight;
    }

    public void setFliteredFlight(List<FlightEntity> fliteredFlight) {
        this.fliteredFlight = fliteredFlight;
    }
    
    
    
}
