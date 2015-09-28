/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.distribution;

import imas.distribution.sessionbean.DistributionSessionBeanLocal;
import imas.inventory.entity.BookingClassEntity;
import imas.inventory.sessionbean.RulesManagementSessionBeanLocal;
import imas.inventory.sessionbean.inventoryRevenueManagementSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.Serializable;
import java.util.ArrayList;
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
@Named(value = "distributionBuyTicketsManagedBean")
@ViewScoped
public class DistributionBuyTicketsManagedBean implements Serializable {

    @EJB
    private DistributionSessionBeanLocal distributionSessionBean;

    @EJB
    private inventoryRevenueManagementSessionBeanLocal inventoryRevenueManagementSessionBean;

    @EJB
    private RulesManagementSessionBeanLocal rulesManagementSessionBean;

    private List<FlightEntity> flights;

    private FlightEntity flight;

    private List<BookingClassEntity> bookingClasses;

    private List<BookingClassEntity> availableBookingClasses;

    private BookingClassEntity bookingClass;

    private Integer purchaseAmount;

    /**
     * Creates a new instance of distributionBuyTicketsManagedBean
     */
    public DistributionBuyTicketsManagedBean() {

    }

    @PostConstruct
    public void init() {
        flights = distributionSessionBean.getAllAvailableFlights();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("allFlights", flights);
    }

    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("allFlights");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("bookingClassList");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("availableBookingClassList");
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

    public List<BookingClassEntity> getBookingClasses() {
        return bookingClasses;
    }

    public void setBookingClasses(List<BookingClassEntity> bookingClasses) {
        this.bookingClasses = bookingClasses;
    }

    public Integer getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Integer purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public BookingClassEntity getBookingClass() {
        return bookingClass;
    }

    public List<BookingClassEntity> getAvailableBookingClasses() {
        return availableBookingClasses;
    }

    public void setAvailableBookingClasses(List<BookingClassEntity> availableBookingClasses) {
        this.availableBookingClasses = availableBookingClasses;
    }

    public void setBookingClass(BookingClassEntity bookingClass) {
        this.bookingClass = bookingClass;
    }

    public void refreshBookingClassseAndAvailableBookingClasses() {
        bookingClasses = inventoryRevenueManagementSessionBean.fetchBookingClass(flight.getId());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bookingClassList", bookingClasses);
        List<BookingClassEntity> availableBookingClassEntities = new ArrayList();
        for (BookingClassEntity bc : bookingClasses) {
            if (bookingClassQuotaLeft(bc) > 0) {
                availableBookingClassEntities.add(bc);
            }
        }
        availableBookingClasses = availableBookingClassEntities;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("availableBookingClassList", bookingClasses);
    }

    public void onFlightChange() {
        if (flight != null) {
            bookingClasses = inventoryRevenueManagementSessionBean.fetchBookingClass(flight.getId());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bookingClassList", bookingClasses);
            List<BookingClassEntity> availableBookingClassEntities = new ArrayList();
            for (BookingClassEntity bc : bookingClasses) {
                if (bookingClassQuotaLeft(bc) > 0) {
                    availableBookingClassEntities.add(bc);
                }
            }
            availableBookingClasses = availableBookingClassEntities;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("availableBookingClassList", bookingClasses);
        }
    }

    public int bookingClassQuotaLeft(BookingClassEntity bookingClass) {
        return distributionSessionBean.getQuotaLeft(bookingClass);
    }

    public void purchase() {
        distributionSessionBean.makeBooking(bookingClass, purchaseAmount);
        bookingClasses = inventoryRevenueManagementSessionBean.fetchBookingClass(flight.getId());
        List<BookingClassEntity> availableBookingClassEntities = new ArrayList();
        for (BookingClassEntity bc : bookingClasses) {
            if (bookingClassQuotaLeft(bc) > 0) {
                availableBookingClassEntities.add(bc);
            }
        }
        availableBookingClasses = availableBookingClassEntities;
    }

}
