/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.distribution.sessionbean.FlightLookupSessionBeanLocal;
import imas.inventory.entity.BookingClassEntity;
import imas.inventory.entity.BookingClassRuleSetEntity;
import imas.inventory.sessionbean.TnCManagementSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.PostRemove;

/**
 *
 * @author Scarlett
 */
@Named(value = "inventoryTnCManagementManagedBean")
@ManagedBean
@ViewScoped
public class InventoryTnCManagementManagedBean implements Serializable {
    
    @EJB
    private TnCManagementSessionBeanLocal tnCManagementSessionBean;

    @EJB
    private FlightLookupSessionBeanLocal flightLookupSessionBean;

    private FlightEntity flight;
    private List<FlightEntity> flights;
    private BookingClassEntity bookingClass;
    private List<BookingClassEntity> bookingClasses;
    private BookingClassRuleSetEntity bookingClassRuleSet;
    private boolean BCSelected;
    
    public InventoryTnCManagementManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        this.flights = flightLookupSessionBean.getAllSellingFlights();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("allFlights", this.flights);
        this.flight = (FlightEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedSellingFlightToManage");
        if (this.flight != null) {
            onFlightChange();
        }
    }

    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("allFlights");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedSellingFlightToManage");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("bookingClassList");
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public List<FlightEntity> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightEntity> flights) {
        this.flights = flights;
    }

    public BookingClassEntity getBookingClass() {
        return bookingClass;
    }

    public void setBookingClass(BookingClassEntity bookingClass) {
        this.bookingClass = bookingClass;
    }

    public List<BookingClassEntity> getBookingClasses() {
        return bookingClasses;
    }

    public void setBookingClasses(List<BookingClassEntity> bookingClasses) {
        this.bookingClasses = bookingClasses;
    }

    public BookingClassRuleSetEntity getBookingClassRuleSet() {
        return bookingClassRuleSet;
    }

    public void setBookingClassRuleSet(BookingClassRuleSetEntity bookingClassRuleSet) {
        this.bookingClassRuleSet = bookingClassRuleSet;
    }

    public boolean isBCSelected() {
        return (flight != null && bookingClass != null);
    }

    public void setBCSelected(boolean BCSelected) {
        this.BCSelected = BCSelected;
    }
    
    public void onFlightChange() {
        if (flight != null) {
            bookingClasses = flight.getBookingClasses();
            bookingClass = null;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bookingClassList", null);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bookingClassList", this.bookingClasses);
        }
    }
    
    public void onBookingClassChange() {
        if (bookingClass != null) {
            bookingClassRuleSet = bookingClass.getBookingClassRuleSet();
        }
    }
    
    public void update() {
        if (bookingClassRuleSet != null) {
            tnCManagementSessionBean.updateTnC(bookingClassRuleSet);
        }
    }
    
    public void returnBack() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("inventoryBookingClassManagement.xhtml");
    }
    
}
