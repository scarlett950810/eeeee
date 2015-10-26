/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.distribution;

import imas.distribution.entity.PNREntity;
import imas.distribution.entity.TicketEntity;
import imas.distribution.sessionbean.ModifyBookingSessionBeanLocal;
import imas.distribution.sessionbean.WebCheckInSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Howard
 */
@ManagedBean
@SessionScoped
public class ModifyBookingManagedBean {

    @EJB
    private WebCheckInSessionBeanLocal webCheckInSessionBean;
    @EJB
    private ModifyBookingSessionBeanLocal modifyBookingSessionBean;

    private String referenceNumber;
    private FlightEntity flight;
    private TicketEntity ticket;
    private List<TicketEntity> tickets;
    private String passportNumber;

    /**
     * Creates a new instance of ModifyBookingManagedBean
     */
    public ModifyBookingManagedBean() {
    }

    @PostConstruct
    public void init() {
    }

    public void seachTicket() throws IOException {
        tickets = modifyBookingSessionBean.getTicketList(referenceNumber, passportNumber);
        if (tickets == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Sorry, this flight does not exist or has expired", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ticketList", tickets);
            FacesContext.getCurrentInstance().getExternalContext().redirect("modifyBookingTicketResult.xhtml");
        }
    }

    public void completeModifyBooking() {
        
        referenceNumber = null;
        passportNumber = null;
    }

    public void startModifyBooking() throws IOException {

        FacesContext.getCurrentInstance().getExternalContext().redirect("modifyBooking.xhtml");

    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

}
