/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.departure;

import imas.departure.sessionbean.PassengerCheckInSessionBeanLocal;
import imas.distribution.entity.TicketEntity;
import imas.planning.entity.FlightEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

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
    private List<TicketEntity> issuedTickets = new ArrayList<TicketEntity>();
    private TicketEntity selectedTicket;
    private TicketEntity issuedSelectedTicket;
    private List<TicketEntity> selectedTickets = new ArrayList<TicketEntity>();
    private Double actualBaggageWeight;
    private Boolean issued;
    private Boolean boarded;

    @PostConstruct
    public void init() {
        fetchFlights();
    }

    public void fetchFlights() {
        comingFlights = passengerCheckInSessionBean.fetchComingFlights(base);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("allFlights", comingFlights);
    }

    public void update() throws IOException {
        if (actualBaggageWeight == null) {
            actualBaggageWeight = 0.0;
        }
        System.out.print(actualBaggageWeight);
        int result = passengerCheckInSessionBean.update(selectedTicket, actualBaggageWeight);
        System.out.print(result);
      
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ticket", selectedTicket);
              
        FacesContext.getCurrentInstance().getExternalContext().redirect("../BoardingPassController");
  
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//        requestContext.execute("PF('ticketDialog').hide()");
        tickets = flight.getTickets();
    }

    public void updateNew() {
        for (int i = 0; i < selectedTickets.size(); i++) {
            passengerCheckInSessionBean.update(selectedTickets.get(i));
            System.out.print(selectedTickets.get(i));
            System.out.print("new new");
        }
//        passengerCheckInSessionBean.update(issuedSelectedTicket);
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("PF('ticketDialog').hide()");
        tickets = flight.getTickets();
        issuedTickets = new ArrayList<TicketEntity>();
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getIssued() == Boolean.TRUE) {
                issuedTickets.add(tickets.get(i));
            }
        }
    }

    public void onFlightChange() {
        if (flight != null) {

            if (flight.getTickets().isEmpty() || flight.getTickets() == null) {
                passengerCheckInSessionBean.intiFFF(flight);
            }
            tickets = flight.getTickets();
//            System.out.print(flight);
            display = true;
//            System.out.print(display);

        }
    }

    public void onFlightChangeNew() {
        if (flight != null) {

            if (flight.getTickets().isEmpty() || flight.getTickets() == null) {
                passengerCheckInSessionBean.intiFFF(flight);
            }
            tickets = flight.getTickets();
            issuedTickets = new ArrayList<TicketEntity>();
            for (int i = 0; i < tickets.size(); i++) {
                if (tickets.get(i).getIssued() == Boolean.TRUE) {
                    issuedTickets.add(tickets.get(i));
                }
            }

            display = true;

        }
    }

    public void updateFlightReportActionListener(ActionEvent event) {
        selectedTicket = (TicketEntity) event.getComponent().getAttributes().get("ticket");
        actualBaggageWeight = selectedTicket.getActualBaggageWeight();
        issued = selectedTicket.getIssued();

        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("PF('ticketDialog').show()");

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

    public TicketEntity getSelectedTicket() {
        return selectedTicket;
    }

    public void setSelectedTicket(TicketEntity selectedTicket) {
        this.selectedTicket = selectedTicket;
    }

    public Double getActualBaggageWeight() {
        return actualBaggageWeight;
    }

    public void setActualBaggageWeight(Double actualBaggageWeight) {
        this.actualBaggageWeight = actualBaggageWeight;
    }

    public Boolean getIssued() {
        return issued;
    }

    public void setIssued(Boolean issued) {
        this.issued = issued;
    }

    public List<TicketEntity> getIssuedTickets() {
        return issuedTickets;
    }

    public void setIssuedTickets(List<TicketEntity> issuedTickets) {
        this.issuedTickets = issuedTickets;
    }

    public TicketEntity getIssuedSelectedTicket() {
        return issuedSelectedTicket;
    }

    public void setIssuedSelectedTicket(TicketEntity issuedSelectedTicket) {
        this.issuedSelectedTicket = issuedSelectedTicket;
    }

    public Boolean getBoarded() {
        return boarded;
    }

    public void setBoarded(Boolean boarded) {
        this.boarded = boarded;
    }

    public List<TicketEntity> getSelectedTickets() {
        return selectedTickets;
    }

    public void setSelectedTickets(List<TicketEntity> selectedTickets) {
        this.selectedTickets = selectedTickets;
    }

}
