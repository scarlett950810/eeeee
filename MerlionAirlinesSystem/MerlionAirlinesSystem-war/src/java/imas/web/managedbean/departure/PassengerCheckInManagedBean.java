/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.departure;

import imas.departure.sessionbean.PassengerCheckInSessionBeanLocal;
import imas.distribution.entity.TicketEntity;
import imas.planning.entity.FlightEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Lei
 */
@Named(value = "passengerCheckInManagedBean")
@ViewScoped
public class PassengerCheckInManagedBean implements Serializable {

    @EJB
    PassengerCheckInSessionBeanLocal passengerCheckInSessionBean;
    private List<FlightEntity> comingFlights;
    private List<FlightEntity> filteredFlights;
    private String base = "SGC";
    private FlightEntity flight;
    private boolean display = false;
    private List<TicketEntity> tickets;

    @PostConstruct
    public void init() {
        fetchFlights();
    }

    public void fetchFlights() {
        comingFlights = passengerCheckInSessionBean.fetchComingFlights(base);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("allFlights", comingFlights);
    }

    public void onFlightChange() {
        if (flight != null) {
            display = true;
            tickets = flight.getTickets();
            System.out.print(flight);

        }
    }

    public List<FlightEntity> getComingFlights() {
        return comingFlights;
    }

    public void setComingFlights(List<FlightEntity> comingFlights) {
        this.comingFlights = comingFlights;
    }

    public List<FlightEntity> getFilteredFlights() {
        return filteredFlights;
    }

    public void setFilteredFlights(List<FlightEntity> filteredFlights) {
        this.filteredFlights = filteredFlights;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public List<TicketEntity> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketEntity> tickets) {
        this.tickets = tickets;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

}
