/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.web.managedbean;

import GDS.entity.GDSAirportEntity;
import GDS.entity.GDSBookingClassEntity;
import GDS.entity.GDSCompanyEntity;
import GDS.entity.GDSFlightEntity;
import GDS.sessionbean.GDSAirportSessionBeanLocal;
import GDS.sessionbean.GDSCompanySessionBeanLocal;
import GDS.sessionbean.GDSFlightSessionBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author Scarlett
 */
@ManagedBean(name = "gDSAddFlightManagedBean")
@SessionScoped
public class GDSAddFlightManagedBean implements Serializable {

    @EJB
    private GDSCompanySessionBeanLocal gDSCompanySessionBean;

    @EJB
    private GDSFlightSessionBeanLocal gDSFlightSessionBean;

    @EJB
    private GDSAirportSessionBeanLocal gDSAirportSessionBean;

    private GDSCompanyEntity company;
    private String flightNo;
    private GDSAirportEntity origin;
    private List<GDSAirportEntity> originCandidates;
    private GDSAirportEntity destination;
    private List<GDSAirportEntity> destinationCandidates;
    private Date today;
    private Date departureDate;
    private Date arrivalMinDate;
    private Date arrivalDate;
    private String aircraftITATCode;
    private int bookingClassNo;
    private List<GDSBookingClassEntity> bookingClasses;
    private List<GDSFlightEntity> companyFlights;
    private GDSFlightEntity selectedFlight;
    private List<GDSBookingClassEntity> companyFlightsBookingClasses;

    public GDSAddFlightManagedBean() {
    }

    @PostConstruct
    public void init() {
        getLoggedinCompany();
        originCandidates = gDSAirportSessionBean.getAllGDSAirport();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("GDSAirportList", originCandidates);
        destinationCandidates = gDSAirportSessionBean.getAllGDSAirport();
        today = new Date();
        arrivalMinDate = today;
    }

    public void getLoggedinCompany() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("account") != null) {
            String account = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("account");
            company = gDSCompanySessionBean.getCompany(account);
        }
    }

    public void onOriginChange() {
        System.out.println("origin = " + origin.getId());

        GDSAirportEntity a = origin;
        destinationCandidates = gDSAirportSessionBean.getAllGDSAirport();
        destinationCandidates.remove(a);
        System.out.println("origin = " + origin);
    }

    public void onDepartureDateSelected() {
        if (arrivalDate != null && departureDate.after(arrivalDate)) {
            arrivalDate = null;
        }
        arrivalMinDate = departureDate;
    }

    public void generateEmptyBookingClasses() {
        bookingClasses = new ArrayList<>();
        for (int i = 0; i < bookingClassNo; i++) {
            GDSBookingClassEntity bc = new GDSBookingClassEntity();
            bookingClasses.add(bc);
        }
        System.out.println("0.5 origin = " + origin);
    }

    public String onFlowProcess(FlowEvent event) {
        System.out.println("0.4 origin = " + origin);
        generateEmptyBookingClasses();
        return event.getNewStep();
    }

    public void addFlightAndBookingClasses() {
        System.out.println("origin" + origin);
        GDSFlightEntity flight = new GDSFlightEntity(company, flightNo, origin, destination, departureDate, arrivalDate, aircraftITATCode);
        gDSFlightSessionBean.generateFlightsAndBookingClasses(flight, bookingClasses);
        FacesMessage msg = new FacesMessage("Flights added successfully", "");
        FacesContext.getCurrentInstance().addMessage("addFlight", msg);
    }

    public void viewFlightBookingClasses(GDSFlightEntity flight) {
        selectedFlight = flight;
        companyFlightsBookingClasses = new ArrayList<>();
        companyFlightsBookingClasses = selectedFlight.getGDSBookingClassEntities();
        RequestContext.getCurrentInstance().execute("PF('companyFlightBookingClasses').show();");
    }

    public GDSCompanyEntity getCompany() {
        return company;
    }

    public void setCompany(GDSCompanyEntity company) {
        this.company = company;
    }

    public GDSAirportEntity getOrigin() {
        return origin;
    }

    public void setOrigin(GDSAirportEntity origin) {
        this.origin = origin;
    }

    public GDSAirportEntity getDestination() {
        return destination;
    }

    public void setDestination(GDSAirportEntity destination) {
        this.destination = destination;
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

    public String getAircraftITATCode() {
        return aircraftITATCode;
    }

    public void setAircraftITATCode(String aircraftITATCode) {
        this.aircraftITATCode = aircraftITATCode;
    }

    public int getBookingClassNo() {
        return bookingClassNo;
    }

    public void setBookingClassNo(int bookingClassNo) {
        this.bookingClassNo = bookingClassNo;
    }

    public List<GDSBookingClassEntity> getBookingClasses() {
        return bookingClasses;
    }

    public void setBookingClasses(List<GDSBookingClassEntity> bookingClasses) {
        this.bookingClasses = bookingClasses;
    }

    public List<GDSAirportEntity> getOriginCandidates() {
        return originCandidates;
    }

    public void setOriginCandidates(List<GDSAirportEntity> originCandidates) {
        this.originCandidates = originCandidates;
    }

    public List<GDSAirportEntity> getDestinationCandidates() {
        return destinationCandidates;
    }

    public void setDestinationCandidates(List<GDSAirportEntity> destinationCandidates) {
        this.destinationCandidates = destinationCandidates;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public Date getArrivalMinDate() {
        return arrivalMinDate;
    }

    public void setArrivalMinDate(Date arrivalMinDate) {
        this.arrivalMinDate = arrivalMinDate;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public List<GDSFlightEntity> getCompanyFlights() {
        getLoggedinCompany();
        return gDSFlightSessionBean.getGDSCompanyFlights(company);
    }

    public void setCompanyFlights(List<GDSFlightEntity> companyFlights) {
        this.companyFlights = companyFlights;
    }

    public List<GDSBookingClassEntity> getCompanyFlightsBookingClasses() {
        return companyFlightsBookingClasses;
    }

    public void setCompanyFlightsBookingClasses(List<GDSBookingClassEntity> companyFlightsBookingClasses) {
        this.companyFlightsBookingClasses = companyFlightsBookingClasses;
    }

    public GDSFlightEntity getSelectedFlight() {
        return selectedFlight;
    }

    public void setSelectedFlight(GDSFlightEntity selectedFlight) {
        this.selectedFlight = selectedFlight;
    }

}
