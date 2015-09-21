/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.inventory.sessionbean.CostManagementSessionBeanLocal;
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
    
        @EJB
    private CostManagementSessionBeanLocal costSessionBean;
  
    
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
        Integer firstClassCapacity = seatsManagementSessionBean.getFirstClassCapacity(flight);
        Integer businessClassCapacity = seatsManagementSessionBean.getBusinessClassCapacity(flight);
        Integer premiumeEonomyClassCapacity = seatsManagementSessionBean.getPremiumEconomyClassCapacity(flight);
        Integer economyClassCapacity = seatsManagementSessionBean.getEconomyClassCapacity(flight);
        Double latestShowRate = seatsManagementSessionBean.computeHistoricalShowRate();
        Integer economyClassComputedOverbookingLevel = (int) (economyClassCapacity / latestShowRate);
        
        // to change to calling session bean.
        Double costPerSeatPerMile = costSessionBean.getCostPerSeatPerMile();
        Double distance = flight.getDistance();
        Double baseFare = costPerSeatPerMile * distance;
        System.out.println("baseFare = " + baseFare);
        seatsManagementSessionBean.generateFirstClassBookingClassEntity(flight, 15 * baseFare, firstClassCapacity);
        seatsManagementSessionBean.generateBusinessClassBookingClassEntity(flight, 6 * baseFare, businessClassCapacity);
        seatsManagementSessionBean.generatePremiumEconomyClassBookingClassEntity(flight, 4 * baseFare, premiumeEonomyClassCapacity);        
        
        // TODO: optimization of yield management.
        seatsManagementSessionBean.generateEconomyClass1BookingClassEntity(flight, 3 * baseFare, 0);
        seatsManagementSessionBean.generateEconomyClass2BookingClassEntity(flight, 2.5 * baseFare, (int) (0.3 * economyClassComputedOverbookingLevel));
        seatsManagementSessionBean.generateEconomyClass3BookingClassEntity(flight, 2 * baseFare, (int) (0.4 * economyClassComputedOverbookingLevel));
        seatsManagementSessionBean.generateEconomyClassAgencyBookingClassEntity(flight, 1.5 * baseFare, (int) (0.1 * economyClassComputedOverbookingLevel));
        seatsManagementSessionBean.generateEconomyClass4BookingClassEntity(flight, 1.1 * baseFare, (int) (0.2 * economyClassComputedOverbookingLevel));
        seatsManagementSessionBean.generateEconomyClass5BookingClassEntity(flight, 0.8 * baseFare, 0);
        
        pendingFlights.remove(flight);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pendingFlights", this.pendingFlights);

    }

}
