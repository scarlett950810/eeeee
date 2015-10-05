/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.distribution;

import imas.distribution.sessionbean.FlightLookupSessionBeanLocal;
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
    // attributes used in this managedBean
    private FlightEntity departureFlight;
    private FlightEntity returnFlight;

    // attributes storing relevant candidates
    private RouteEntity departureDirectRoute;
    private RouteEntity returnDirectRoute;
    private List<RouteEntity> departureTransfer1Route;
    private List<RouteEntity> returnTransfer1Route;
    
    private List<FlightEntity> departureDirectFlightCandidates;
    private List<FlightEntity> returnDirectFlightCandidates;
    private List<FlightEntity> departureTransfer1FlightCandidates;
    private List<FlightEntity> returnTransfer1FlightCandidates;

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
        String seatClass
                = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("seatClass");
        Integer adultNo
                = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("adultNo");
        Integer childNo
                = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("childNo");
        Integer infantNo
                = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("infantNo");
        departureDirectRoute
                = flightLookupSessionBean.getDirectRoutes(orginAirport, destinationAirport);
        System.out.println("departureDirectRoute = " + departureDirectRoute);
        
        returnDirectRoute
                = flightLookupSessionBean.getDirectRoutes(destinationAirport, orginAirport);        
        System.out.println("returnDirectRoute = " + returnDirectRoute);
        
        departureDirectFlightCandidates
                = flightLookupSessionBean.getAvailableFlights(departureDirectRoute, departureDate);
        returnDirectFlightCandidates
                = flightLookupSessionBean.getAvailableFlights(returnDirectRoute, returnDate);
        
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

    public List<RouteEntity> getDepartureTransfer1Route() {
        return departureTransfer1Route;
    }

    public void setDepartureTransfer1Route(List<RouteEntity> departureTransfer1Route) {
        this.departureTransfer1Route = departureTransfer1Route;
    }

    public List<RouteEntity> getReturnTransfer1Route() {
        return returnTransfer1Route;
    }

    public void setReturnTransfer1Route(List<RouteEntity> returnTransfer1Route) {
        this.returnTransfer1Route = returnTransfer1Route;
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

    public List<FlightEntity> getDepartureTransfer1FlightCandidates() {
        return departureTransfer1FlightCandidates;
    }

    public void setDepartureTransfer1FlightCandidates(List<FlightEntity> departureTransfer1FlightCandidates) {
        this.departureTransfer1FlightCandidates = departureTransfer1FlightCandidates;
    }

    public List<FlightEntity> getReturnTransfer1FlightCandidates() {
        return returnTransfer1FlightCandidates;
    }

    public void setReturnTransfer1FlightCandidates(List<FlightEntity> returnTransfer1FlightCandidates) {
        this.returnTransfer1FlightCandidates = returnTransfer1FlightCandidates;
    }

}
