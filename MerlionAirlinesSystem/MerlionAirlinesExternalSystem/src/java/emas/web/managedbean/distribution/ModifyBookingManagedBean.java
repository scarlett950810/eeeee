/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.distribution;

import imas.distribution.entity.PNREntity;
import imas.distribution.entity.TicketEntity;
import imas.distribution.sessionbean.ModifyBookingSessionBeanLocal;
import imas.departure.sessionbean.WebCheckInSessionBeanLocal;
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
    private ModifyBookingSessionBeanLocal modifyBookingSessionBean;

    private String referenceNumber;
    private FlightEntity flight;
    private TicketEntity ticket;
    private List<TicketEntity> tickets;
    private String passportNumber;
    private double extraBaggageWeight;
    private boolean premiumMeal;
    private boolean insurance;
    private boolean exclusiveService;
    private boolean flightWifi;
    private double extraPrice;

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
            context.addMessage(null, new FacesMessage("Sorry, the reference number or passport number is invalid or the flight has expired", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ticketList", tickets);
            FacesContext.getCurrentInstance().getExternalContext().redirect("modifyBookingTicketResult.xhtml");
        }
    }

    public void upgradePremiumService(){
        System.out.print(extraBaggageWeight + "," + premiumMeal + "," + insurance + "," + exclusiveService + "," + flightWifi);
    }
    
    public void completeModifyBooking() throws IOException {
        ticket.setBaggageWeight(extraBaggageWeight);
        ticket.setPremiumMeal(premiumMeal);
        ticket.setInsurance(insurance);
        ticket.setExclusiveService(exclusiveService);
        ticket.setFlightWiFi(flightWifi);
        
        modifyBookingSessionBean.flushModification(ticket);
        referenceNumber = null;
        passportNumber = null;
        flight = null;
        tickets = null;
        ticket = null;
        FacesContext.getCurrentInstance().getExternalContext().redirect("https://localhost:8181/MerlionAirlinesExternalSystem/index.xhtml");
    }

    public void startModifyBooking() throws IOException {
        extraBaggageWeight = ticket.getBaggageWeight();
        premiumMeal = ticket.getPremiumMeal();
        insurance = ticket.getInsurance();
        exclusiveService = ticket.getExclusiveService();
        flightWifi = ticket.getFlightWiFi();
        System.out.print(extraBaggageWeight + "," + premiumMeal + "," + insurance + "," + exclusiveService + "," + flightWifi);
    
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

    public double getExtraBaggageWeight() {
        return extraBaggageWeight;
    }

    public void setExtraBaggageWeight(double extraBaggageWeight) {
        this.extraBaggageWeight = extraBaggageWeight;
    }

    public boolean isPremiumMeal() {
        return premiumMeal;
    }

    public void setPremiumMeal(boolean premiumMeal) {
        this.premiumMeal = premiumMeal;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    public boolean isExclusiveService() {
        return exclusiveService;
    }

    public void setExclusiveService(boolean exclusiveService) {
        this.exclusiveService = exclusiveService;
    }

    public boolean isFlightWifi() {
        return flightWifi;
    }

    public void setFlightWifi(boolean flightWifi) {
        this.flightWifi = flightWifi;
    }

    public double getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(double extraPrice) {
        this.extraPrice = extraPrice;
    }
}
