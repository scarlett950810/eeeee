/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.operation;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.operation.sessionbean.CrewCheckInSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Howard
 */
@Named(value = "crewCheckInManagedBean")
@ViewScoped
public class CrewCheckInManagedBean implements Serializable{
    @EJB
    private CrewCheckInSessionBeanLocal crewCheckInSessionBean;

    
    private String base = "SIN";
    private List<FlightEntity> flights;
    private FlightEntity flight;
    private List<PilotEntity> pilotList;
    private List<CabinCrewEntity> crewList;
    private List<PilotEntity> selectedPilots;
    private List<CabinCrewEntity> selectedCrew;
    private boolean display = false;
    
    /**
     * Creates a new instance of CrewCheckInManagedBean
     */
    public CrewCheckInManagedBean() {
    }
    
    @PostConstruct
    public void init(){
        fetchFlights();
    }
    
    public void fetchFlights(){
        flights = crewCheckInSessionBean.fetchFlights(base);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("allFlights", flights);
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public List<FlightEntity> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightEntity> flights) {
        this.flights = flights;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public List<PilotEntity> getPilotList() {
        return pilotList;
    }

    public void setPilotList(List<PilotEntity> pilotList) {
        this.pilotList = pilotList;
    }

    public List<CabinCrewEntity> getCrewList() {
        return crewList;
    }

    public void setCrewList(List<CabinCrewEntity> crewList) {
        this.crewList = crewList;
    }

    public List<PilotEntity> getSelectedPilots() {
        return selectedPilots;
    }

    public void setSelectedPilots(List<PilotEntity> selectedPilots) {
        this.selectedPilots = selectedPilots;
    }

    public List<CabinCrewEntity> getSelectedCrew() {
        return selectedCrew;
    }

    public void setSelectedCrew(List<CabinCrewEntity> selectedCrew) {
        this.selectedCrew = selectedCrew;
    }
    
    public void doCrewCheckIn() throws IOException{
        crewCheckInSessionBean.doCrewCheckIn(selectedPilots, selectedCrew);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("operationCrewCheckIn.xhtml");
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }
    
    public void onFlightChange(){
        if(flight != null){
            pilotList = flight.getPilots();
            crewList = flight.getCabinCrews();
            display = true;
            System.out.print(flight);
        }
    }
}
