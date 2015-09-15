/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.inventory.entity.BookingClassEntity;
import imas.inventory.sessionbean.inventoryRevenueManagementSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.IOException;
import java.io.Serializable;
import static java.lang.Boolean.TRUE;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Howard
 */
@Named(value = "inventoryRevenueManagementManagedBean")
@SessionScoped
public class InventoryRevenueManagementManagedBean implements Serializable{
    @EJB
    private inventoryRevenueManagementSessionBeanLocal inventoryRevenueManagementSessionBean;

    private List<FlightEntity> flightList;
    private List<FlightEntity> fliteredFlight;
    private FlightEntity selectedFlight;
    private List<BookingClassEntity> bookingClassList;
    private BookingClassEntity economyOne;
    private BookingClassEntity economyTwo;
    private BookingClassEntity economyThree;
    private BookingClassEntity bookingClass;
    private double newPricing;
    private boolean close = true; // This attribute is to identify whether the quota dialog box is closed by the cross or not

    
    public InventoryRevenueManagementManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        fetchFlights();
        
    }
    
    public void fetchFlights(){
        flightList = inventoryRevenueManagementSessionBean.fetchFlight();
    }

    public List<FlightEntity> getFlightList() {
        return flightList;
    }

    public void setFlightList(List<FlightEntity> flightList) {
        this.flightList = flightList;
    }

    public List<FlightEntity> getFliteredFlight() {
        return fliteredFlight;
    }

    public void setFliteredFlight(List<FlightEntity> fliteredFlight) {
        this.fliteredFlight = fliteredFlight;
    }

    public FlightEntity getSelectedFlight() {
        return selectedFlight;
    }

    public void setSelectedFlight(FlightEntity selectedFlight) {
        this.selectedFlight = selectedFlight;
    }

    public List<BookingClassEntity> getBookingClassList() {
        return bookingClassList;
    }

    public void setBookingClassList(List<BookingClassEntity> bookingClassList) {
        this.bookingClassList = bookingClassList;
    }
    
    
    public void viewBookingClass() throws IOException{
        bookingClassList = inventoryRevenueManagementSessionBean.fetchBookingClass(selectedFlight.getId());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bookingClassList", bookingClassList);
        
        for (BookingClassEntity bookingClassList1 : bookingClassList) {
            switch (bookingClassList1.getName()) {
                case "Economy Class 1":
                    economyOne = bookingClassList1;
                    break;
                case "Economy Class 2":
                    economyTwo = bookingClassList1;
                    break;
                case "Economy Class 3":
                    economyThree = bookingClassList1;
                    break;
            }
        }
        
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("inventoryBookingClassManagement.xhtml");
    }
    
    public Integer getSoldSeats(Long bookingClassID){
        return inventoryRevenueManagementSessionBean.computeSoldSeats(selectedFlight.getId(), bookingClassID);
    }
    
    public void returnBack() throws IOException{
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("inventoryRevenueManagement.xhtml");
    }

    public void updateBookingClassQuota() throws IOException{
        
        inventoryRevenueManagementSessionBean.updateBookingClassQuota(economyOne.getId(), economyOne.getQuota());
        inventoryRevenueManagementSessionBean.updateBookingClassQuota(economyTwo.getId(), economyTwo.getQuota());
        inventoryRevenueManagementSessionBean.updateBookingClassQuota(economyThree.getId(), economyThree.getQuota());
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("inventoryBookingClassManagement.xhtml");
    }

    public BookingClassEntity getEconomyOne() {
        return economyOne;
    }

    public void setEconomyOne(BookingClassEntity economyOne) {
        this.economyOne = economyOne;
    }

    public BookingClassEntity getEconomyTwo() {
        return economyTwo;
    }

    public void setEconomyTwo(BookingClassEntity economyTwo) {
        this.economyTwo = economyTwo;
    }

    public BookingClassEntity getEconomyThree() {
        return economyThree;
    }

    public void setEconomyThree(BookingClassEntity economyThree) {
        this.economyThree = economyThree;
    }

    public BookingClassEntity getBookingClass() {
        return bookingClass;
    }

    public void setBookingClass(BookingClassEntity bookingClass) {
        this.bookingClass = bookingClass;
    }

    public double getNewPricing() {
        return newPricing;
    }

    public void setNewPricing(double newPricing) {
        this.newPricing = newPricing;
    }
    
    public void updatePricing() throws IOException{
        inventoryRevenueManagementSessionBean.updateBookingClassPricing(bookingClass.getId(), newPricing);
        bookingClassList = inventoryRevenueManagementSessionBean.fetchBookingClass(selectedFlight.getId());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bookingClassList", bookingClassList);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("inventoryBookingClassManagement.xhtml");
        System.out.print("invoked");
    }
    
    public void closeQuotaForm(){
        if(close == true){
            System.out.print(economyOne.getQuota());
            System.out.print(economyTwo.getQuota());
            System.out.print(economyThree.getQuota());
        }
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    
    
    
}

