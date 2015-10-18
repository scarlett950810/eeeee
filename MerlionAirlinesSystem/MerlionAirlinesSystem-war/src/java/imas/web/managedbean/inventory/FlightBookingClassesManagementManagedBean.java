/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.inventory.sessionbean.CostManagementSessionBeanLocal;
import imas.inventory.sessionbean.BookingClassesManagementSessionBeanLocal;
import imas.inventory.sessionbean.YieldManagementSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.persistence.PostRemove;

/**
 *
 * @author Scarlett
 */
@Named(value = "flightBookingClassesManagementManagedBean")
@ViewScoped
public class FlightBookingClassesManagementManagedBean implements Serializable {

    @EJB
    private YieldManagementSessionBeanLocal yieldManagementSessionBean;

    @EJB
    private BookingClassesManagementSessionBeanLocal bookingClassesManagementSessionBean;

    @EJB
    private CostManagementSessionBeanLocal costSessionBean;

    private List<FlightEntity> pendingFlights;

    /**
     * Creates a new instance of SeatsManagementManagedBean
     */
    public FlightBookingClassesManagementManagedBean() {
    }

    @PostConstruct
    public void init() {
        this.pendingFlights = bookingClassesManagementSessionBean.getFlightCandidateToOpenForBooking();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pendingFlights", this.pendingFlights);
//        bookingClassesManagementSessionBean.insertData();
    }

    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pendingFlights");
    }

    public BookingClassesManagementSessionBeanLocal getSeatsManagementSessionBean() {
        return bookingClassesManagementSessionBean;
    }

    public void setSeatsManagementSessionBean(BookingClassesManagementSessionBeanLocal bookingClassesManagementSessionBean) {
        this.bookingClassesManagementSessionBean = bookingClassesManagementSessionBean;
    }

    public List<FlightEntity> getPendingFlights() {
        return pendingFlights;
    }

    public void setPendingFlights(List<FlightEntity> pendingFlights) {
        this.pendingFlights = pendingFlights;
    }

    public void automaticallyCreateBookingClassAndYieldManagementRules(FlightEntity flight) {
        createBookingClasses(flight);
        yieldManagementSessionBean.autoCreateRulesForFlight(flight);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful",
                "Booking classes and rules for " + flight.getFlightNo() + " created."));
    }

    public void createBookingClasses(FlightEntity flight) {
        bookingClassesManagementSessionBean.createBookingClassesAndYieldManagementRules(flight);
/*
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("Creating Booking Classes for flight " + flight.getFlightNo() + " at " + flight.getDepartureDate() + ":");

        Integer firstClassCapacity = bookingClassesManagementSessionBean.getFirstClassCapacity(flight);
        Integer businessClassCapacity = bookingClassesManagementSessionBean.getBusinessClassCapacity(flight);
        Integer premiumEconomyClassCapacity = bookingClassesManagementSessionBean.getPremiumEconomyClassCapacity(flight);
        Integer economyClassCapacity = bookingClassesManagementSessionBean.getEconomyClassCapacity(flight);
        Double latestShowRate = bookingClassesManagementSessionBean.computeHistoricalShowRate(flight.getRoute());
        Integer economyClassComputedOverbookingLevel = (int) (economyClassCapacity / latestShowRate);

        // to change to calling session bean.
        Double costPerSeatPerMile = costSessionBean.getCostPerSeatPerMile(flight.getRoute());
        Double distance = flight.getRoute().getDistance();
        Double baseFare = costPerSeatPerMile * distance;
//        System.out.println("baseFare = " + baseFare);
        bookingClassesManagementSessionBean.generateFirstClassBookingClassEntityAndTAndC(flight, 15 * baseFare, firstClassCapacity);
        bookingClassesManagementSessionBean.generateBusinessClassBookingClassEntityAndTAndC(flight, 6 * baseFare, businessClassCapacity);
        bookingClassesManagementSessionBean.generatePremiumEconomyClassBookingClassEntity(flight, 4 * baseFare, premiumEconomyClassCapacity);

        // TODO: optimization of yield management.
        bookingClassesManagementSessionBean.generateEconomyClass1BookingClassEntity(flight, 3 * baseFare, 0);
        bookingClassesManagementSessionBean.generateEconomyClass2BookingClassEntity(flight, 2.5 * baseFare, (int) (0.3 * economyClassComputedOverbookingLevel));
        bookingClassesManagementSessionBean.generateEconomyClass3BookingClassEntity(flight, 2 * baseFare, (int) (0.4 * economyClassComputedOverbookingLevel));
        bookingClassesManagementSessionBean.generateEconomyClassAgencyBookingClassEntity(flight, 1.5 * baseFare, (int) (0.1 * economyClassComputedOverbookingLevel));
        bookingClassesManagementSessionBean.generateEconomyClass4BookingClassEntity(flight, 1.1 * baseFare, (int) (0.2 * economyClassComputedOverbookingLevel));
        bookingClassesManagementSessionBean.generateEconomyClass5BookingClassEntity(flight, 0.8 * baseFare, 0);

        System.out.println("Basefare = Cost per seat per mile (" + costPerSeatPerMile + ") * Distance (" + distance + ")");
        System.out.println("         = " + baseFare);
        System.out.println("=========================================================================");

        System.out.println("First Class capacity = " + firstClassCapacity);
        System.out.println();
        System.out.println("First Class price = 15 * base fare = " + 15 * baseFare);
        System.out.println("First Class quota = " + firstClassCapacity);
        System.out.println("=========================================================================");
        System.out.println();

        System.out.println("Business Class capacity = " + businessClassCapacity);
        System.out.println();
        System.out.println("Business Class price = 6 * base fare = " + 6 * baseFare);
        System.out.println("Business Class quota = " + businessClassCapacity);
        System.out.println("=========================================================================");
        System.out.println();

        System.out.println("Premium Eonomy Class Capacity = " + premiumEconomyClassCapacity);
        System.out.println();
        System.out.println("Premium Eonomy Class price = 4 * base fare = " + 4 * baseFare);
        System.out.println("Premium Eonomy Class quota = " + premiumEconomyClassCapacity);
        System.out.println("=========================================================================");
        System.out.println();

        System.out.println("Eonomy Class Capacity = " + economyClassCapacity);
        System.out.println("");
        System.out.println("The lastest show rate = " + latestShowRate);
        System.out.println("Economy class over booking level = Economy class capacity (" + economyClassCapacity + ")"
                + " ÷ show rate (" + latestShowRate + ")");
        System.out.println("                                 = " + economyClassComputedOverbookingLevel);
        System.out.println("Economy Class 1:        quota = 0                                                  price = 3 * base fare = " + 3 * baseFare);
        System.out.println("Economy Class 2:        quota = 0.3 * economy class computed overbooking level = " + (int) (0.3 * economyClassComputedOverbookingLevel) + "  |  price = 2.5 * base fare = " + 2.5 * baseFare);
        System.out.println("Economy Class 3:        quota = 0.4 * economy class computed overbooking level = " + (int) (0.4 * economyClassComputedOverbookingLevel) + "  |  price = 2 * base fare = " + 2 * baseFare);
        System.out.println("Economy Class Agency:   quota = 0.1 * economy class computed overbooking level = " + (int) (0.1 * economyClassComputedOverbookingLevel) + "  |  price = 1.5 * base fare = " + 1.5 * baseFare);
        System.out.println("Economy Class 4:        quota = 0.2 * economy class computed overbooking level = " + (int) (0.2 * economyClassComputedOverbookingLevel) + "  |  price = 1.1 * base fare = " + 1.1 * baseFare);
        System.out.println("Economy Class 5:        quota = 0                                                  price = 0.8 * base fare = " + 0.8 * baseFare);
        System.out.println("=========================================================================");
        System.out.println("......Done");
*/
        pendingFlights.remove(flight);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pendingFlights", this.pendingFlights);

    }

    public Integer getDaysToDeparture(FlightEntity flight) {
        return yieldManagementSessionBean.getFromNowToDepartureInDay(flight);
    }
}
