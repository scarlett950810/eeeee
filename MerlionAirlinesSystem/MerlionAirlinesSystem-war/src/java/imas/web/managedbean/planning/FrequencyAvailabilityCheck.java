/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
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
@Named(value = "frequencyAvailabilityCheck")
@ViewScoped
public class FrequencyAvailabilityCheck implements Serializable{

    private List<FlightEntity> flightsUnassigned;
    private List<FlightEntity> flightsToTest;
    @EJB
    private RouteSessionBeanLocal routeSession;
    private Boolean canSupport = false;
    private Boolean canNotSupport = false;
    private Integer numOfUnassignedFlights = 0;
    private RouteEntity routeSelected;
    private Integer yearSelected;
    private List<FlightEntity> flightsUnassignedPilot;
    private List<FlightEntity> flightsUnassignedCabinCrew;
    private Integer numOfUnassignedFlightsPilot = 0;
    private Integer numOfUnassignedFlightsCabin = 0;
    /**
     * Creates a new instance of FrequencyAvailabilityCheck
     */
    public FrequencyAvailabilityCheck() {
    }

    @PostConstruct
    public void init() {
        System.out.println("init()");
        flightsToTest = (List<FlightEntity>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("flightsToTest");
        routeSelected = (RouteEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("routeSelected");

        yearSelected = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("yearSelected");
        flightsUnassignedPilot = (List<FlightEntity>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("flightsUnassignedPilot");
        flightsUnassignedCabinCrew = (List<FlightEntity>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("flightsUnassignedCabinCrew");

        flightsUnassigned = (List<FlightEntity>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("flightsUnassigned");
        
        if ((flightsUnassigned.isEmpty()||flightsUnassigned.size()<= 2)&&flightsUnassignedPilot.isEmpty()&&flightsUnassignedCabinCrew.isEmpty()) {
            canSupport = true;
        } else {
           numOfUnassignedFlights = flightsUnassigned.size();
           numOfUnassignedFlightsPilot = flightsUnassignedPilot.size();
            numOfUnassignedFlightsCabin = flightsUnassignedCabinCrew.size();
            canNotSupport = true;
        }
        
    }

    public void generateFlights() throws IOException {
        for (FlightEntity f : flightsToTest) {
            routeSession.saveReturnFlights(f);
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("yearSelected", yearSelected);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("routeSelected", routeSelected);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningDisplayFlightsGenerated.xhtml");
    }

    public void goBack() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningSetFrequency.xhtml");
    }

    public Integer getNumOfUnassignedFlightsPilot() {
        return numOfUnassignedFlightsPilot;
    }

    public void setNumOfUnassignedFlightsPilot(Integer numOfUnassignedFlightsPilot) {
        this.numOfUnassignedFlightsPilot = numOfUnassignedFlightsPilot;
    }

    public Integer getNumOfUnassignedFlightsCabin() {
        return numOfUnassignedFlightsCabin;
    }

    public void setNumOfUnassignedFlightsCabin(Integer numOfUnassignedFlightsCabin) {
        this.numOfUnassignedFlightsCabin = numOfUnassignedFlightsCabin;
    }

    public List<FlightEntity> getFlightsToTest() {
        return flightsToTest;
    }

    public void setFlightsToTest(List<FlightEntity> flightsToTest) {
        this.flightsToTest = flightsToTest;
    }

    public RouteEntity getRouteSelected() {
        return routeSelected;
    }

    public void setRouteSelected(RouteEntity routeSelected) {
        this.routeSelected = routeSelected;
    }

    public Integer getYearSelected() {
        return yearSelected;
    }

    public void setYearSelected(Integer yearSelected) {
        this.yearSelected = yearSelected;
    }

    public List<FlightEntity> getFlightsUnassignedPilot() {
        return flightsUnassignedPilot;
    }

    public void setFlightsUnassignedPilot(List<FlightEntity> flightsUnassignedPilot) {
        this.flightsUnassignedPilot = flightsUnassignedPilot;
    }

    public List<FlightEntity> getFlightsUnassignedCabinCrew() {
        return flightsUnassignedCabinCrew;
    }

    public void setFlightsUnassignedCabinCrew(List<FlightEntity> flightsUnassignedCabinCrew) {
        this.flightsUnassignedCabinCrew = flightsUnassignedCabinCrew;
    }

    public Integer getNumOfUnassignedFlights() {
        return numOfUnassignedFlights;
    }

    public void setNumOfUnassignedFlights(Integer numOfUnassignedFlights) {
        this.numOfUnassignedFlights = numOfUnassignedFlights;
    }

    public Boolean getCanNotSupport() {
        return canNotSupport;
    }

    public void setCanNotSupport(Boolean canNotSupport) {
        this.canNotSupport = canNotSupport;
    }

    public List<FlightEntity> getFlightsUnassigned() {
        return flightsUnassigned;
    }

    public void setFlightsUnassigned(List<FlightEntity> flightsUnassigned) {
        this.flightsUnassigned = flightsUnassigned;
    }

    public Boolean getCanSupport() {
        return canSupport;
    }

    public void setCanSupport(Boolean canSupport) {
        this.canSupport = canSupport;
    }

}
