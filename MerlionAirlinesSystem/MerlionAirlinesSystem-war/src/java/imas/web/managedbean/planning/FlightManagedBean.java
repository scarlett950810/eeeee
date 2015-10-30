/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.operation.sessionbean.CrewToFlightSessionBeanLocal;
import imas.planning.entity.AircraftEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.AircraftSessionBeanLocal;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.Temporal;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author wutong
 */
@Named(value = "flightManagedBean")
@SessionScoped
public class FlightManagedBean implements Serializable {

    private String flightNo;
    private String returnFlightNo;
    private List<RouteEntity> routes;
    private RouteEntity route;
    private List<AircraftEntity> aircrafts;
    private AircraftEntity aircraft;
    private List<PilotEntity> pilots;
    private List<PilotEntity> selectedPilots;
    private PilotEntity pilot;
    private List<CabinCrewEntity> cabinCrewList;
    private List<CabinCrewEntity> selectedCabinCrewList;
    private FlightEntity flightForDelete;
    private List<FlightEntity> flights;
    private String message;
    @EJB
    private AircraftSessionBeanLocal aircraftSession;
    @EJB
    private CrewToFlightSessionBeanLocal crewToFlightSession;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date departureDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date arrivalDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date departureDateR;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date arrivalDateR;

    /**
     * Creates a new instance of FlightManagedBean
     */
    public FlightManagedBean() {
    }

    @PostConstruct
    public void init() {
        System.err.println("create new flight managed bean");
        flights = crewToFlightSession.retrieveAllFlights();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("allFlights", flights);
        System.err.println("create new flight managed bean" + flights.size());
    }

//    @PostConstruct
//    public void init() {
//        getAircrafts();
//        System.err.println("PRINT3"+aircrafts);
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("aircraftList", aircrafts);
//    }
    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getReturnFlightNo() {
        return returnFlightNo;
    }

    public void setReturnFlightNo(String returnFlightNo) {
        this.returnFlightNo = returnFlightNo;
    }

    public List<RouteEntity> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteEntity> routes) {
        this.routes = routes;
    }

    public RouteEntity getRoute() {
        return route;
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public List<AircraftEntity> getAircrafts() {
        if (route != null && departureDate != null && arrivalDate != null) {
            System.err.println("Enter getAircrafts" + route.getRouteName() + departureDate + arrivalDate);
            aircrafts = aircraftSession.retrieveAvailableAircrafts(route, departureDate, arrivalDate);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("aircraftList", aircrafts);
            return aircrafts;
        }
        return null;
    }

    public void setAircrafts(List<AircraftEntity> aircrafts) {
        this.aircrafts = aircrafts;
    }

    public AircraftEntity getAircraft() {
        return aircraft;
    }

    public void setAircraft(AircraftEntity aircraft) {
        System.err.println("PRINT2" + aircraft);
        this.aircraft = aircraft;
    }

    public Date getDepartureDateR() {
        return departureDateR;
    }

    public void setDepartureDateR(Date departureDateR) {
        this.departureDateR = departureDateR;
    }

    public Date getArrivalDateR() {
        return arrivalDateR;
    }

    public void setArrivalDateR(Date arrivalDateR) {
        this.arrivalDateR = arrivalDateR;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PilotEntity> getPilots() {
        System.err.println("enter get pilots" + route + aircraft + departureDate + arrivalDate);        
        pilots = crewToFlightSession.retreiveAvailablePilot(aircraft, route, departureDate, arrivalDate);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pilotList", pilots);        
        System.err.println("Complete get pilots in managed bean");
        return pilots;
    }

    public void setPilots(List<PilotEntity> pilots) {
        this.pilots = pilots;
    }

    public List<FlightEntity> getFlights() {
        flights = crewToFlightSession.retrieveAllFlights();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("allFlights", flights);
        return flights;
    }

    public void setFlights(List<FlightEntity> flights) {
        this.flights = flights;
    }

    public int getPilotNumber() {
        if (route.getDistance() < 5000) {
            return 2;
        } else {
            return 3;
        }
    }

    public int getCabinCrewNumber() {
        int size = aircraft.getSeats().size();
        int numOfCrew = size / 50;
        if (size % 50 > 0) {
            numOfCrew++;
        }

        return numOfCrew;
    }

    public List<CabinCrewEntity> getCabinCrewList() {
        System.err.println("enter get cabin crew" + route + aircraft + departureDate + arrivalDate);
        cabinCrewList = crewToFlightSession.retrieveAvailableCabinCrew(route, departureDate, arrivalDate);
        System.err.println("PRINTHAHA" + cabinCrewList);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cabinCrewList", cabinCrewList);
        System.err.println("complete get cabin crew in managed bean");
        return cabinCrewList;
    }

    public void setCabinCrewList(List<CabinCrewEntity> cabinCrewList) {
        this.cabinCrewList = cabinCrewList;
    }

    public List<CabinCrewEntity> getSelectedCabinCrewList() {
        return selectedCabinCrewList;
    }

    public void setSelectedCabinCrewList(List<CabinCrewEntity> selectedCabinCrewList) {
        this.selectedCabinCrewList = selectedCabinCrewList;
    }

    public PilotEntity getPilot() {
        return pilot;
    }

    public void setPilot(PilotEntity pilot) {
        this.pilot = pilot;
    }

    public List<PilotEntity> getSelectedPilots() {
        return selectedPilots;
    }

    public void setSelectedPilots(List<PilotEntity> selectedPilots) {
        this.selectedPilots = selectedPilots;
    }

    public FlightEntity getFlightForDelete() {
        return flightForDelete;
    }

    public void setFlightForDelete(FlightEntity flightForDelete) {
        this.flightForDelete = flightForDelete;
    }

    public void dateSelectListenerOneWay(SelectEvent event) {
        System.err.println("Hello");
        //       departureDate = (Date) event.getComponent().getAttributes().get("departureD");

//        System.err.println("flightEntity: " + flightEntity.getDepartureDate());
        Calendar cal = Calendar.getInstance();
        System.err.println(departureDate + "Route: " + route);
        cal.setTime(departureDate);

        cal.add(Calendar.MINUTE, (int) (route.getFlightHours() * 60 + 0.5d));
//        Date halfHourBack = cal.getTime();
        arrivalDate = cal.getTime();

//        flightEntity.setArrivalDate(halfHourBack);
        cal.add(Calendar.MINUTE, 60);
        departureDateR = cal.getTime();

////        flightEntity.getReverseFlight().setDepartureDate(cal.getTime());
        cal.add(Calendar.MINUTE, (int) (route.getFlightHours() * 60 + 0.5d));
        arrivalDateR = cal.getTime();
//        flightEntity.getReverseFlight().setArrivalDate(cal.getTime());
    }

    public void dateSelectListenerReturnWay(SelectEvent event) {
        System.err.println("Hello");
        //       departureDateR = (Date) event.getComponent().getAttributes().get("departureDR");

//        System.err.println("flightEntity: " + flightEntity.getDepartureDate());
        Calendar cal = Calendar.getInstance();
        cal.setTime(departureDateR);

        cal.add(Calendar.MINUTE, (int) (route.getFlightHours() * 60 + 0.5d));
//        Date halfHourBack = cal.getTime();
        arrivalDateR = cal.getTime();

//        flightEntity.setArrivalDate(halfHourBack);
//        flightEntity.getReverseFlight().setArrivalDate(cal.getTime());
    }

    public void onRouteChange() {
        if (route != null & departureDate != null) {
            Calendar cal = Calendar.getInstance();
            System.err.println(departureDate + "Route: " + route);
            cal.setTime(departureDate);

            cal.add(Calendar.MINUTE, (int) (route.getFlightHours() * 60 + 0.5d));
            arrivalDate = cal.getTime();
        }
    }

    public String getTimeName() {
        if (arrivalDate == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        return dateFormat.format(arrivalDate);

    }

    public String getReturnTimeName() {
        if (arrivalDateR == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        return dateFormat.format(arrivalDateR);

    }

    public void goAddCrew() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningAddCrewToFlight.xhtml");
    }

    public void goConfirmFlight() throws IOException {
        int p = getPilotNumber();
        int c = getCabinCrewNumber();
        FacesContext context = FacesContext.getCurrentInstance();
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        if (selectedPilots.size() != p) {
            context.addMessage(null, new FacesMessage("", "Please only select " + p + " pilots"));
        } else if (selectedCabinCrewList.size() != c) {
            context.addMessage(null, new FacesMessage("", "Please only select " + c + " cabin crew"));
        } else {
            createFlight();
            flights = crewToFlightSession.retrieveAllFlights();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("allFlights", flights);
            ec.redirect("planningConfirmFlight.xhtml");
            context.addMessage(null, new FacesMessage("Successful", "Add successfully"));
        }

    }

    public void createFlight() {
        FlightEntity f = new FlightEntity();
        Calendar cal = Calendar.getInstance();
        cal.setTime(departureDate);
        Integer opYear = cal.get(Calendar.YEAR);
        f.setOperatingYear(opYear);
        f.setDepartureDate(departureDate);
        f.setArrivalDate(arrivalDate);
        f.setAircraft(aircraft);
        f.setFlightNo(flightNo);
        f.setCabinCrews(selectedCabinCrewList);
        f.setPilots(selectedPilots);
        f.setRoute(route);        
        
        FlightEntity returnF = new FlightEntity();
        cal.setTime(departureDateR);
        Integer opYearR = cal.get(Calendar.YEAR);
        returnF.setOperatingYear(opYearR);
        returnF.setDepartureDate(departureDateR);
        returnF.setArrivalDate(arrivalDateR);
        returnF.setFlightNo(returnFlightNo);
        returnF.setAircraft(aircraft);
        returnF.setCabinCrews(selectedCabinCrewList);
        returnF.setPilots(selectedPilots);
        returnF.setRoute(route.getReverseRoute());
        f.setReverseFlight(returnF);
        returnF.setReverseFlight(f);
        crewToFlightSession.createFlight(f, returnF);
        
    }

    public void deleteFlight() {
        FacesContext context = FacesContext.getCurrentInstance();
        FlightEntity returnF = flightForDelete.getReverseFlight();
        System.err.println("Flight for delete exist" + flightForDelete);
        System.err.println("Flight for return exist" + returnF);
        crewToFlightSession.deleteFlight(flightForDelete);
        flights = crewToFlightSession.retrieveAllFlights();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("allFlights", flights);
        //crewToFlightSession.deleteFlight(returnF);
        context.addMessage(null, new FacesMessage("", "Delete successfully"));
    }
    
    public void goAddFlight() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningAddFlight.xhtml");
    }
    
    public void goDeleteFlight() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningDeleteFlight.xhtml");
    }
}
