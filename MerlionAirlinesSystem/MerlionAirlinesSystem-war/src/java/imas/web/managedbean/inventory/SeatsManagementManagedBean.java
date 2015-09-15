/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.inventory.entity.BookingClassEntity;
import imas.inventory.sessionbean.SeatsManagementSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.persistence.PostRemove;

/**
 *
 * @author Scarlett
 */
@Named(value = "seatsManagementManagedBean")
@ViewScoped
public class SeatsManagementManagedBean implements Serializable {
    @EJB
    private SeatsManagementSessionBeanLocal seatsManagementSessionBean;
    
//    private FlightEntity flight;
    
    private List<FlightEntity> pendingFlights;
    
//    private int economyClassComputedOverbookingLevel;
    
//    private int economyClassOverbookingLevel;
    
//    private int economyClassCapacity;

    /**
     * Creates a new instance of SeatsManagementManagedBean
     */
    public SeatsManagementManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        this.pendingFlights = seatsManagementSessionBean.getFlightsWithoutBookingClass();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pendingFlights", this.pendingFlights);
//        seatsManagementSessionBean.insertData();
    }

    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pendingFlights");
    }

    public SeatsManagementSessionBeanLocal getSeatsManagementSessionBean() {
        return seatsManagementSessionBean;
    }

    public void setSeatsManagementSessionBean(SeatsManagementSessionBeanLocal seatsManagementSessionBean) {
        this.seatsManagementSessionBean = seatsManagementSessionBean;
    }

//    public FlightEntity getFlight() {
//        return flight;
//    }
//
//    public void setFlight(FlightEntity flight) {
//        this.flight = flight;
//    }

    public List<FlightEntity> getPendingFlights() {
        return pendingFlights;
    }

    public void setPendingFlights(List<FlightEntity> pendingFlights) {
        this.pendingFlights = pendingFlights;
    }

//    public int getEconomyClassComputedOverbookingLevel() {
//        return economyClassComputedOverbookingLevel;
//    }
//
//    public void setEconomyClassComputedOverbookingLevel(int economyClassComputedOverbookingLevel) {
//        this.economyClassComputedOverbookingLevel = economyClassComputedOverbookingLevel;
//    }
//
//    public int getEconomyClassOverbookingLevel() {
//        return economyClassOverbookingLevel;
//    }
//
//    public void setEconomyClassOverbookingLevel(int economyClassOverbookingLevel) {
//        this.economyClassOverbookingLevel = economyClassOverbookingLevel;
//    }
//
//    public int getEconomyClassCapacity() {
//        return economyClassCapacity;
//    }

//    public void setEconomyClassCapacity(int economyClassCapacity) {
//        this.economyClassCapacity = economyClassCapacity;
//    }
       
//    public void onFlightChange() {
//        System.out.println("onFlightChange");
//        System.out.println(this.flight);
//        if (this.flight != null) {
//            System.out.println("getEconomyClassCapacity");
//            System.out.println(seatsManagementSessionBean.getEconomyClassCapacity(this.flight));
//            economyClassCapacity = seatsManagementSessionBean.getEconomyClassCapacity(this.flight);
//        }
//    }
    public void automaticallySetPrice(FlightEntity flight) {
        int firstClassCapacity = seatsManagementSessionBean.getFirstClassCapacity(flight);
        int businessClassCapacity = seatsManagementSessionBean.getBusinessClassCapacity(flight);
        int premiumeEonomyClassCapacity = seatsManagementSessionBean.getPremiumEconomyClassCapacity(flight);
        int economyClassCapacity = seatsManagementSessionBean.getEconomyClassCapacity(flight);
        double latestShowRate = seatsManagementSessionBean.computeHistoricalShowRate();
        int economyClassComputedOverbookingLevel = (int) (economyClassCapacity / latestShowRate);
        
        
        
        
        
        
    }

}
