/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.distribution;

import imas.distribution.sessionbean.FlightLookupSessionBeanLocal;
import imas.distribution.sessionbean.TransferFlight;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

/**
 *
 * @author Scarlett
 */
@Named(value = "selectFlightManagedBean")
@SessionScoped
public class SelectFlightManagedBean implements Serializable {

    @EJB
    private FlightLookupSessionBeanLocal flightLookupSessionBean;

    /*
     // attributes from flightLookupManagedBean
     private AirportEntity orginAirport;
     private AirportEntity destinationAirport;
     private boolean twoWay;
     private Date departureDate;
     private Date returnDate;
     private String seatClass;
     private int adultNo;
     private int childNo;
     private int infantNo;
     */
    
    // attributes used in this managedBean to pass to booking management
    private FlightEntity departureFlight;
    private FlightEntity departureTransferFlight1;
    private FlightEntity departureTransferFlight2;
    private FlightEntity returnFlight;
    private FlightEntity returnTransferFlight1;
    private FlightEntity returnTransferFlight2;
    
    // attributes used only for select or display
    private boolean departureHasDirectFlight;
    private boolean departureHasTransferFlight;
    private boolean returnHasDirectFlight;
    private boolean returnHasTransferFlight;

    private RouteEntity departureDirectRoute;
    private RouteEntity returnDirectRoute;
    private List<FlightEntity> departureDirectFlightCandidates;
    private List<FlightEntity> returnDirectFlightCandidates;
    private List<TransferFlight> departureTransferFlightCandidates;
    private List<TransferFlight> returnTransferFlightCandidates;

    private String seatClass;
    /**
     * Creates a new instance of SelectFlightManagedBean
     */
    public SelectFlightManagedBean() {

    }

    @PostConstruct
    public void init() {

        AirportEntity orginAirport
                = (AirportEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("orginAirport");
        AirportEntity destinationAirport
                = (AirportEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("destinationAirport");
        boolean twoWay
                = (boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("twoWay");
        Date departureDate
                = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("departureDate");
        Date returnDate
                = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("returnDate");
        seatClass
                = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("seatClass");
        Integer adultNo
                = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("adultNo");
        Integer childNo
                = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("childNo");
        Integer infantNo
                = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("infantNo");

        // load departure flight data
        departureDirectRoute
                = flightLookupSessionBean.getDirectRoute(orginAirport, destinationAirport);
        if (departureDirectRoute != null) {
            departureDirectFlightCandidates
                    = flightLookupSessionBean.getAvailableFlights(departureDirectRoute, departureDate, flightLookupSessionBean.getDateAfterDays(departureDate, 1));
            departureHasDirectFlight = (departureDirectFlightCandidates.size() > 0);
        } else {
            departureHasDirectFlight = false;
        }
        departureTransferFlightCandidates = flightLookupSessionBean.getTransferRoutes(orginAirport, destinationAirport, departureDate);
        departureHasTransferFlight = (departureTransferFlightCandidates.size() > 0);

        // loading return flight data
        returnDirectRoute
                = flightLookupSessionBean.getDirectRoute(destinationAirport, orginAirport);
        if (returnDirectRoute != null) {
            returnDirectFlightCandidates
                    = flightLookupSessionBean.getAvailableFlights(returnDirectRoute, returnDate, flightLookupSessionBean.getDateAfterDays(returnDate, 1));
        }
        returnTransferFlightCandidates = flightLookupSessionBean.getTransferRoutes(orginAirport, destinationAirport, returnDate);
        returnHasTransferFlight = (returnTransferFlightCandidates.size() > 0);
    }

    public FlightEntity getDepartureFlight() {
        return departureFlight;
    }

    public void setDepartureFlight(FlightEntity departureFlight) {
        this.departureFlight = departureFlight;
    }

    public FlightEntity getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(FlightEntity returnFlight) {
        this.returnFlight = returnFlight;
    }

    public FlightLookupSessionBeanLocal getFlightLookupSessionBean() {
        return flightLookupSessionBean;
    }

    public void setFlightLookupSessionBean(FlightLookupSessionBeanLocal flightLookupSessionBean) {
        this.flightLookupSessionBean = flightLookupSessionBean;
    }

    public RouteEntity getDepartureDirectRoute() {
        return departureDirectRoute;
    }

    public void setDepartureDirectRoute(RouteEntity departureDirectRoute) {
        this.departureDirectRoute = departureDirectRoute;
    }

    public RouteEntity getReturnDirectRoute() {
        return returnDirectRoute;
    }

    public void setReturnDirectRoute(RouteEntity returnDirectRoute) {
        this.returnDirectRoute = returnDirectRoute;
    }

    public List<FlightEntity> getDepartureDirectFlightCandidates() {
        return departureDirectFlightCandidates;
    }

    public void setDepartureDirectFlightCandidates(List<FlightEntity> departureDirectFlightCandidates) {
        this.departureDirectFlightCandidates = departureDirectFlightCandidates;
    }

    public List<FlightEntity> getReturnDirectFlightCandidates() {
        return returnDirectFlightCandidates;
    }

    public void setReturnDirectFlightCandidates(List<FlightEntity> returnDirectFlightCandidates) {
        this.returnDirectFlightCandidates = returnDirectFlightCandidates;
    }

    public boolean isDepartureHasDirectFlight() {
        return departureHasDirectFlight;
    }

    public void setDepartureHasDirectFlight(boolean departureHasDirectFlight) {
        this.departureHasDirectFlight = departureHasDirectFlight;
    }

    public boolean isDepartureHasTransferFlight() {
        return departureHasTransferFlight;
    }

    public void setDepartureHasTransferFlight(boolean departureHasTransferFlight) {
        this.departureHasTransferFlight = departureHasTransferFlight;
    }

    public boolean isReturnHasDirectFlight() {
        return returnHasDirectFlight;
    }

    public void setReturnHasDirectFlight(boolean returnHasDirectFlight) {
        this.returnHasDirectFlight = returnHasDirectFlight;
    }

    public boolean isReturnHasTransferFlight() {
        return returnHasTransferFlight;
    }

    public void setReturnHasTransferFlight(boolean returnHasTransferFlight) {
        this.returnHasTransferFlight = returnHasTransferFlight;
    }

    public List<TransferFlight> getDepartureTransferFlightCandidates() {
        return departureTransferFlightCandidates;
    }

    public void setDepartureTransferFlightCandidates(List<TransferFlight> departureTransferFlightCandidates) {
        this.departureTransferFlightCandidates = departureTransferFlightCandidates;
    }

    public List<TransferFlight> getReturnTransferFlightCandidates() {
        return returnTransferFlightCandidates;
    }

    public void setReturnTransferFlightCandidates(List<TransferFlight> returnTransferFlightCandidates) {
        this.returnTransferFlightCandidates = returnTransferFlightCandidates;
    }

    public FlightEntity getDepartureTransferFlight1() {
        return departureTransferFlight1;
    }

    public void setDepartureTransferFlight1(FlightEntity departureTransferFlight1) {
        this.departureTransferFlight1 = departureTransferFlight1;
    }

    public FlightEntity getDepartureTransferFlight2() {
        return departureTransferFlight2;
    }

    public void setDepartureTransferFlight2(FlightEntity departureTransferFlight2) {
        this.departureTransferFlight2 = departureTransferFlight2;
    }

    public FlightEntity getReturnTransferFlight1() {
        return returnTransferFlight1;
    }

    public void setReturnTransferFlight1(FlightEntity returnTransferFlight1) {
        this.returnTransferFlight1 = returnTransferFlight1;
    }

    public FlightEntity getReturnTransferFlight2() {
        return returnTransferFlight2;
    }

    public void setReturnTransferFlight2(FlightEntity returnTransferFlight2) {
        this.returnTransferFlight2 = returnTransferFlight2;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public String getUserFriendlyTime(double hours) {
        int hourNo = (int) hours;
        int minNo = (int) (60 * (hours - hourNo));
        return hourNo + " hour " + minNo + " mins";
    }
    
    public double getLowestFare(FlightEntity flight) {
        return flightLookupSessionBean.getLowestFareAvailable(flight, seatClass);
    }

}
