/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.departure;

import imas.distribution.entity.TicketEntity;
import imas.departure.sessionbean.SeatHelperClass;
import imas.departure.sessionbean.WebCheckInSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author Howard
 */
@Named
@SessionScoped
public class WebCheckInManagedBean implements Serializable {

    @EJB
    private WebCheckInSessionBeanLocal webCheckInSessionBean;

    private String referenceNumber;
    private String passportNumber;
    private FlightEntity flight;
    private TicketEntity ticket;
    private List<TicketEntity> tickets;
    private List<List<SeatHelperClass>> seatHelper = new ArrayList<>();
    private SeatHelperClass currentSeat;

    public void seachTicket() throws IOException {
        tickets = webCheckInSessionBean.getCheckInTicket(passportNumber, referenceNumber);
        System.out.print(tickets);
        if (tickets == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Sorry, this flight does not exist", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ticketList", tickets);
            FacesContext.getCurrentInstance().getExternalContext().redirect("webCheckInTicketResult.xhtml");
        }
    }

    public void completeWebCheckIn() throws IOException {
        if (currentSeat != null) {
            webCheckInSessionBean.completeWebCheckIn(seatHelper, ticket);
            System.err.println(ticket);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ticket", ticket);
            referenceNumber = null;
            passportNumber = null;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("ticketList");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../BoardingPassController");
        }
    }

    public void startWebCheckIn() throws IOException {
        Date currentDate = new Date();
//        if (ticket.getFlight().getDepartureDate().getTime() - currentDate.getTime() > 259200000) {
//            FacesContext context = FacesContext.getCurrentInstance();
//            context.addMessage(null, new FacesMessage("You can only perform web check-in 3 days prior to the flight departure", ""));
//        } else if (ticket.getFlight().getDepartureDate().getTime() - currentDate.getTime() < 3600000) {
//            FacesContext context = FacesContext.getCurrentInstance();
//            context.addMessage(null, new FacesMessage("Web Check-in is now closed for" + ticket.getFlight().getFlightNo(), ""));
//        } else {
        seatHelper = webCheckInSessionBean.fetchAllSeats(ticket.getFlight().getAircraft().getId(), ticket.getFlight().getId(), ticket.getBookingClassName());
        FacesContext.getCurrentInstance().getExternalContext().redirect("webCheckInSelectSeat.xhtml");
//        }
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public void onChangeSeat(SeatHelperClass seatHelperClass) {
        if (currentSeat != null) {
            currentSeat.setSelected(false);
        }
        currentSeat = seatHelperClass;
//        System.out.print(seatHelperClass);
    }

    public WebCheckInManagedBean() {
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public List<List<SeatHelperClass>> getSeatHelper() {
        return seatHelper;
    }

    public void setSeatHelper(List<List<SeatHelperClass>> seatHelper) {
        this.seatHelper = seatHelper;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public TicketEntity getTicket() {
        return ticket;
    }

    public void setTicket(TicketEntity ticket) {
        this.ticket = ticket;
    }

    public List<TicketEntity> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketEntity> tickets) {
        this.tickets = tickets;
    }

    public SeatHelperClass getCurrentSeat() {
        return currentSeat;
    }

    public void setCurrentSeat(SeatHelperClass currentSeat) {
        this.currentSeat = currentSeat;
    }

}
