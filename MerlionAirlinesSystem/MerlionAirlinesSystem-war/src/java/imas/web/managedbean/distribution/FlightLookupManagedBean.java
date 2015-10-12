/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.distribution;

import imas.distribution.entity.PassengerEntity;
import imas.distribution.entity.TicketEntity;
import imas.distribution.sessionbean.FlightLookupSessionBeanLocal;
import imas.distribution.sessionbean.TransferFlight;
import imas.inventory.entity.BookingClassEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.AircraftSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.persistence.PostRemove;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Scarlett
 */
@ManagedBean
@SessionScoped
public class FlightLookupManagedBean implements Serializable {

    @EJB
    private AircraftSessionBeanLocal aircraftSessionBean;

    @EJB
    private FlightLookupSessionBeanLocal flightLookupSessionBean;

    private AirportEntity orginAirport;
    private AirportEntity destinationAirport;
    private boolean twoWay;
    private Date departureDate;
    private Date returnDate;
    private String seatClass;
    private int adultNo;
    private int childNo;
    private int infantNo;

    private Date departureMinDate;
    private Date departureMaxDate;
    private Date returnMinDate;
    private Date returnMaxDate;

    private List<AirportEntity> airportList;
    private List<SelectItem> airportsByCountry;
    private List<SelectItem> destinationAirportsByCountry;
    private String returnDateDisplay;

    // selected flights and bookingClasses
    private FlightEntity departureDirectFlight;
    private FlightEntity departureTransferFlight1;
    private FlightEntity departureTransferFlight2;
    private FlightEntity returnDirectFlight;
    private FlightEntity returnTransferFlight1;
    private FlightEntity returnTransferFlight2;
    private BookingClassEntity departureDirectFlightBookingClass;
    private BookingClassEntity departureTransferFlight1BookingClass;
    private BookingClassEntity departureTransferFlight2BookingClass;
    private BookingClassEntity returnDirectFlightBookingClass;
    private BookingClassEntity returnTransferFlight1BookingClass;
    private BookingClassEntity returnTransferFlight2BookingClass;

    // for displaying flights options
    private boolean showTransferOptions;
    private int activeIndex;
    private boolean tab1Disabled;
    private boolean tab2Disabled;
    private boolean tab3Disabled;
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

    // for displaying booking class options
    private List<BookingClassEntity> departureDirectFlightBookingClassCandidates;
    private List<BookingClassEntity> departureTransferFlight1BookingClassCandidates;
    private List<BookingClassEntity> departureTransferFlight2BookingClassCandidates;
    private List<BookingClassEntity> returnDirectFlightBookingClassCandidates;
    private List<BookingClassEntity> returnTransferFlight1BookingClassCandidates;
    private List<BookingClassEntity> returnTransferFlight2BookingClassCandidates;

    @PostConstruct
    public void init() {

        fetchAllAirports();
        airportList = aircraftSessionBean.getAirports();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("airportList", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("airportList", airportList);

        twoWay = true;
        Calendar today = Calendar.getInstance();
        departureMinDate = today.getTime();
        returnMinDate = today.getTime();
        today.add(Calendar.YEAR, 1);
        departureMaxDate = today.getTime();
        returnMaxDate = today.getTime();
        activeIndex = 0;
        tab1Disabled = false;
        tab2Disabled = true;
        tab3Disabled = true;

    }

    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("airportList");
    }

    public FlightEntity getDepartureDirectFlight() {
        return departureDirectFlight;
    }

    public void setDepartureDirectFlight(FlightEntity departureDirectFlight) {
        this.departureDirectFlight = departureDirectFlight;
    }

    public FlightEntity getReturnDirectFlight() {
        return returnDirectFlight;
    }

    public void setReturnDirectFlight(FlightEntity returnDirectFlight) {
        this.returnDirectFlight = returnDirectFlight;
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

    public FlightLookupManagedBean() {
    }

    public AirportEntity getOrginAirport() {
        return orginAirport;
    }

    public void setOrginAirport(AirportEntity orginAirport) {
        this.orginAirport = orginAirport;
    }

    public AirportEntity getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(AirportEntity destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public List<SelectItem> getAirportsByCountry() {
        return airportsByCountry;
    }

    public void setAirportsByCountry(List<SelectItem> airportsByCountry) {
        this.airportsByCountry = airportsByCountry;
    }

    /**
     * Get the value of airportList
     *
     * @return the value of airportList
     */
    public List<AirportEntity> getAirportList() {
        return airportList;
    }

    /**
     * Set the value of airportList
     *
     * @param airportList new value of airportList
     */
    public void setAirportList(List<AirportEntity> airportList) {
        this.airportList = airportList;
    }

    public List<SelectItem> getDestinationAirportsByCountry() {
        return destinationAirportsByCountry;
    }

    public void setDestinationAirportsByCountry(List<SelectItem> destinationAirportsByCountry) {
        this.destinationAirportsByCountry = destinationAirportsByCountry;
    }

    public boolean isTwoWay() {
        return twoWay;
    }

    public void setTwoWay(boolean twoWay) {
        this.twoWay = twoWay;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public int getAdultNo() {
        return adultNo;
    }

    public void setAdultNo(int adultNo) {
        this.adultNo = adultNo;
    }

    public int getChildNo() {
        return childNo;
    }

    public void setChildNo(int childNo) {
        this.childNo = childNo;
    }

    public int getInfantNo() {
        return infantNo;
    }

    public void setInfantNo(int infantNo) {
        this.infantNo = infantNo;
    }

    public Date getDepartureMinDate() {
        return departureMinDate;
    }

    public void setDepartureMinDate(Date departureMinDate) {
        this.departureMinDate = departureMinDate;
    }

    public Date getDepartureMaxDate() {
        return departureMaxDate;
    }

    public void setDepartureMaxDate(Date departureMaxDate) {
        this.departureMaxDate = departureMaxDate;
    }

    public Date getReturnMinDate() {
        return returnMinDate;
    }

    public void setReturnMinDate(Date returnMinDate) {
        this.returnMinDate = returnMinDate;
    }

    public Date getReturnMaxDate() {
        return returnMaxDate;
    }

    public void setReturnMaxDate(Date returnMaxDate) {
        this.returnMaxDate = returnMaxDate;
    }

    public boolean isTab1Disabled() {
        return tab1Disabled;
    }

    public void setTab1Disabled(boolean tab1Disabled) {
        this.tab1Disabled = tab1Disabled;
    }

    public boolean isTab2Disabled() {
        return tab2Disabled;
    }

    public void setTab2Disabled(boolean tab2Disabled) {
        this.tab2Disabled = tab2Disabled;
    }

    public boolean isTab3Disabled() {
        return tab3Disabled;
    }

    public void setTab3Disabled(boolean tab3Disabled) {
        this.tab3Disabled = tab3Disabled;
    }

    public String getReturnDateDisplay() {
        if (twoWay) {
            return "display: block";
        } else {
            return "display: none";
        }
    }

    public void setReturnDateDisplay(String returnDateDisplay) {
        this.returnDateDisplay = returnDateDisplay;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public boolean isShowTransferOptions() {
        return showTransferOptions;
    }

    public void setShowTransferOptions(boolean showTransferOptions) {
        this.showTransferOptions = showTransferOptions;
    }

    public BookingClassEntity getDepartureDirectFlightBookingClass() {
        return departureDirectFlightBookingClass;
    }

    public void setDepartureDirectFlightBookingClass(BookingClassEntity departureDirectFlightBookingClass) {
        this.departureDirectFlightBookingClass = departureDirectFlightBookingClass;
    }

    public BookingClassEntity getDepartureTransferFlight1BookingClass() {
        return departureTransferFlight1BookingClass;
    }

    public void setDepartureTransferFlight1BookingClass(BookingClassEntity departureTransferFlight1BookingClass) {
        this.departureTransferFlight1BookingClass = departureTransferFlight1BookingClass;
    }

    public BookingClassEntity getDepartureTransferFlight2BookingClass() {
        return departureTransferFlight2BookingClass;
    }

    public void setDepartureTransferFlight2BookingClass(BookingClassEntity departureTransferFlight2BookingClass) {
        this.departureTransferFlight2BookingClass = departureTransferFlight2BookingClass;
    }

    public BookingClassEntity getReturnDirectFlightBookingClass() {
        return returnDirectFlightBookingClass;
    }

    public void setReturnDirectFlightBookingClass(BookingClassEntity returnDirectFlightBookingClass) {
        this.returnDirectFlightBookingClass = returnDirectFlightBookingClass;
    }

    public BookingClassEntity getReturnTransferFlight1BookingClass() {
        return returnTransferFlight1BookingClass;
    }

    public void setReturnTransferFlight1BookingClass(BookingClassEntity returnTransferFlight1BookingClass) {
        this.returnTransferFlight1BookingClass = returnTransferFlight1BookingClass;
    }

    public BookingClassEntity getReturnTransferFlight2BookingClass() {
        return returnTransferFlight2BookingClass;
    }

    public void setReturnTransferFlight2BookingClass(BookingClassEntity returnTransferFlight2BookingClass) {
        this.returnTransferFlight2BookingClass = returnTransferFlight2BookingClass;
    }

    public List<BookingClassEntity> getDepartureDirectFlightBookingClassCandidates() {
        return departureDirectFlightBookingClassCandidates;
    }

    public void setDepartureDirectFlightBookingClassCandidates(List<BookingClassEntity> departureDirectFlightBookingClassCandidates) {
        this.departureDirectFlightBookingClassCandidates = departureDirectFlightBookingClassCandidates;
    }

    public List<BookingClassEntity> getDepartureTransferFlight1BookingClassCandidates() {
        return departureTransferFlight1BookingClassCandidates;
    }

    public void setDepartureTransferFlight1BookingClassCandidates(List<BookingClassEntity> departureTransferFlight1BookingClassCandidates) {
        this.departureTransferFlight1BookingClassCandidates = departureTransferFlight1BookingClassCandidates;
    }

    public List<BookingClassEntity> getDepartureTransferFlight2BookingClassCandidates() {
        return departureTransferFlight2BookingClassCandidates;
    }

    public void setDepartureTransferFlight2BookingClassCandidates(List<BookingClassEntity> departureTransferFlight2BookingClassCandidates) {
        this.departureTransferFlight2BookingClassCandidates = departureTransferFlight2BookingClassCandidates;
    }

    public List<BookingClassEntity> getReturnDirectFlightBookingClassCandidates() {
        return returnDirectFlightBookingClassCandidates;
    }

    public void setReturnDirectFlightBookingClassCandidates(List<BookingClassEntity> returnDirectFlightBookingClassCandidates) {
        this.returnDirectFlightBookingClassCandidates = returnDirectFlightBookingClassCandidates;
    }

    public List<BookingClassEntity> getReturnTransferFlight1BookingClassCandidates() {
        return returnTransferFlight1BookingClassCandidates;
    }

    public void setReturnTransferFlight1BookingClassCandidates(List<BookingClassEntity> returnTransferFlight1BookingClassCandidates) {
        this.returnTransferFlight1BookingClassCandidates = returnTransferFlight1BookingClassCandidates;
    }

    public List<BookingClassEntity> getReturnTransferFlight2BookingClassCandidates() {
        return returnTransferFlight2BookingClassCandidates;
    }

    public void setReturnTransferFlight2BookingClassCandidates(List<BookingClassEntity> returnTransferFlight2BookingClassCandidates) {
        this.returnTransferFlight2BookingClassCandidates = returnTransferFlight2BookingClassCandidates;
    }

    public String getUserFriendlyTime(double hours) {
        int hourNo = (int) hours;
        int minNo = (int) (60 * (hours - hourNo));
        return hourNo + " hour " + minNo + " mins";
    }

    public int getLowestFare(FlightEntity flight) {
        List<BookingClassEntity> bcs = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(flight, seatClass);
        double lowestFare = Double.MAX_VALUE;
        for (BookingClassEntity bc : bcs) {
            if (bc.getPrice() < lowestFare) {
                lowestFare = bc.getPrice();
            }
        }
        return (int) lowestFare;
    }

    private void fetchAllAirports() {
        airportsByCountry = new ArrayList<>();
        List<String> countries = flightLookupSessionBean.getAllCountryNames();
        for (String country : countries) {
            SelectItemGroup group = new SelectItemGroup(country);
            List<AirportEntity> airportsInCountry = flightLookupSessionBean.getAllAirportsInCountry(country);

            SelectItem[] selectItems = new SelectItem[airportsInCountry.size()];

            for (int i = 0; i < airportsInCountry.size(); i++) {
                AirportEntity airport = airportsInCountry.get(i);
                SelectItem selectItem = new SelectItem(airport, airport.toString());
                selectItems[i] = selectItem;
            }
            group.setSelectItems(selectItems);
            airportsByCountry.add(group);
        }

        destinationAirportsByCountry = airportsByCountry;
    }

    /*    
     // obsoleted. 
     // used to get airports that are linked by direct flight and update the dropdown list for destination.
     // using this method user can only select direct flights.
     private void fetchDestinationAirports(AirportEntity origAirport) {
     destinationAirportsByCountry = new ArrayList<>();

     List<AirportEntity> allDestinationAirports = flightLookupSessionBean.getAllDestinationAirports(origAirport);
     List<String> allCountries = flightLookupSessionBean.getAllCountryNames();
     for (String country : allCountries) {
     List<AirportEntity> destinationAirportsInCountry = new ArrayList();
     for (AirportEntity a : allDestinationAirports) {
     if (a.getNationName().equals(country)) {
     destinationAirportsInCountry.add(a);
     }
     }
     if (!destinationAirportsInCountry.isEmpty()) {
     SelectItemGroup group = new SelectItemGroup(country);
     SelectItem[] selectItems = new SelectItem[destinationAirportsInCountry.size()];
     for (int i = 0; i < destinationAirportsInCountry.size(); i++) {
     AirportEntity airport = destinationAirportsInCountry.get(i);
     SelectItem selectItem = new SelectItem(airport, airport.getAirportName() + " (" + airport.getAirportCode() + ")");
     selectItems[i] = selectItem;
     }
     group.setSelectItems(selectItems);
     destinationAirportsByCountry.add(group);
     }
     }
     }
     */
    // remove orgin airport from destination airport list
    private void updateDestinationAirportList() {

        destinationAirportsByCountry = new ArrayList<>();
        List<String> countries = flightLookupSessionBean.getAllCountryNames();
        for (String country : countries) {
            SelectItemGroup group = new SelectItemGroup(country);
            List<AirportEntity> airportsInCountry = flightLookupSessionBean.getAllAirportsInCountry(country);

            SelectItem[] selectItems = new SelectItem[airportsInCountry.size()];

            for (int i = 0; i < airportsInCountry.size(); i++) {
                AirportEntity airport = airportsInCountry.get(i);
                SelectItem selectItem = new SelectItem(airport, airport.toString());

                if (airport.equals(orginAirport)) {
                    selectItem.setDisabled(true);
                }

                selectItems[i] = selectItem;
            }
            group.setSelectItems(selectItems);
            destinationAirportsByCountry.add(group);

        }

    }

    public void onOriginChange() {
        if (orginAirport != null) {
            updateDestinationAirportList();
        }
    }

    public void onDepartureDateSelect(SelectEvent event) {
        if (twoWay) {
            returnMinDate = departureDate;
            if (returnDate != null && departureDate.after(returnDate)) {
                returnDate = null;
            }
        }
    }

    public void onReturnDateSelect(SelectEvent event) {
        departureMaxDate = returnDate;
        if (departureDate != null && departureDate.after(returnDate)) {
            departureDate = null;
        }
    }

    public void searchFlight() {
        initSelectFlight();
        tab2Disabled = false;
        activeIndex = 1;
    }

    public void searchFromHomePage() throws IOException {
        initSelectFlight();
        activeIndex = 1;
        tab2Disabled = false;
        FacesContext.getCurrentInstance().getExternalContext().redirect("selectFlight.xhtml");
    }

    public void initSelectFlight() {

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

        if (twoWay) {
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
        showTransferOptions = true;
        tab2Disabled = false;

    }

    public boolean selectedDepartureDirectFlight() {
        return departureDirectFlight != null && departureTransferFlight1 == null && departureTransferFlight2 == null;
    }

    public boolean selectedDepartureTransferFlight() {
        return departureDirectFlight == null && departureTransferFlight1 != null && departureTransferFlight2 != null;
    }

    public boolean selectedNoDepartureFlights() {
        return departureDirectFlight == null && departureTransferFlight1 == null && departureTransferFlight2 == null;
    }

    public boolean selectedReturnDirectFlight() {
        return returnDirectFlight != null && returnTransferFlight1 == null && returnTransferFlight2 == null;
    }

    public boolean selectedReturnTransferFlight() {
        return returnDirectFlight == null && returnTransferFlight1 != null && returnTransferFlight2 != null;
    }

    public boolean selectedNoReturnFlights() {
        return returnDirectFlight == null && returnTransferFlight1 == null && returnTransferFlight2 == null;
    }

    public void submitFlightsToSelectBookingClasses() {
        if (checkFlightsSubmitted()) {
            initSelectBookingClass();
            activeIndex = 2;
            tab3Disabled = false;
        } else {
            activeIndex = 1;
            tab3Disabled = true;
        }
    }

    public boolean checkFlightsSubmitted() {
        boolean flag = true;

        if (!selectedDepartureDirectFlight() && !selectedDepartureTransferFlight() && !selectedNoDepartureFlights()) {
            flag = false;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "For departure, please select one direct flight or a pair of transfer flights", ""));
        }

        if (twoWay) {
            if (!selectedReturnDirectFlight() && !selectedReturnTransferFlight() && !selectedNoReturnFlights()) {
                flag = false;
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "For return, please select one direct flight or a pair of transfer flights", ""));
            }
        }
        return flag;
    }

    public void initSelectBookingClass() {

        List<List<BookingClassEntity>> bookingClassLists = new ArrayList<>();

        if (selectedDepartureDirectFlight()) {
            List<BookingClassEntity> bookingClassList
                    = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(departureDirectFlight, seatClass);
            departureDirectFlightBookingClassCandidates = bookingClassList;
        } else if (selectedDepartureTransferFlight()) {
            List<BookingClassEntity> bookingClassList1
                    = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(departureTransferFlight1, seatClass);
            List<BookingClassEntity> bookingClassList2
                    = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(departureTransferFlight2, seatClass);
            departureTransferFlight1BookingClassCandidates = bookingClassList1;
            departureTransferFlight2BookingClassCandidates = bookingClassList2;
        }

        if (twoWay) {
            if (selectedReturnDirectFlight()) {
                List<BookingClassEntity> bookingClassList
                        = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(returnDirectFlight, seatClass);
                returnDirectFlightBookingClassCandidates = bookingClassList;
            } else if (selectedReturnTransferFlight()) {
                List<BookingClassEntity> bookingClassList1
                        = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(returnTransferFlight1, seatClass);
                List<BookingClassEntity> bookingClassList2
                        = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(returnTransferFlight2, seatClass);
                returnTransferFlight1BookingClassCandidates = bookingClassList1;
                returnTransferFlight2BookingClassCandidates = bookingClassList2;
            }
        }

    }

    public boolean checkBookingClassesSubmitted() {
        boolean flag = true;

        if (selectedDepartureDirectFlight() && departureDirectFlightBookingClass == null) {
            flag = false;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "For your departure flight, please select booking class", ""));
        } else if (selectedDepartureTransferFlight() && (returnTransferFlight1BookingClass == null || returnTransferFlight2BookingClass == null)) {
            flag = false;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "For your departure flight, please select booking class for both transfer flights", ""));
        }
        if (selectedReturnDirectFlight() && returnDirectFlightBookingClass == null) {
            flag = false;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "For your return flight, please select booking class", ""));
        } else if (selectedReturnTransferFlight() && (returnTransferFlight1BookingClass == null || returnTransferFlight2BookingClass == null)) {
            flag = false;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "For your return flight, please select booking class for both transfer flights", ""));
        }
        return flag;
    }

    public void submitBookingClasses() {

        if (checkBookingClassesSubmitted()) {
            List<PassengerEntity> passengers = new ArrayList<>();
            List<FlightEntity> flights = new ArrayList<>();

            for (int i = 0; i < adultNo + childNo + infantNo; i++) {
                PassengerEntity passenger = new PassengerEntity();
                TicketEntity ticket1 = null, ticket2 = null, ticket3 = null, ticket4 = null, ticket5 = null, ticket6 = null;
                if (selectedDepartureDirectFlight()) {
                    flights.add(departureDirectFlight);
                    ticket1 = new TicketEntity(departureDirectFlight, departureDirectFlightBookingClass.getName(), departureDirectFlightBookingClass.getPrice(), passenger);
                } else if (selectedDepartureTransferFlight()) {
                    flights.add(departureTransferFlight1);
                    ticket2 = new TicketEntity(departureTransferFlight1, departureTransferFlight1BookingClass.getName(), departureTransferFlight1BookingClass.getPrice(), passenger);
                    flights.add(departureTransferFlight1);
                    ticket3 = new TicketEntity(departureTransferFlight1, departureTransferFlight1BookingClass.getName(), departureTransferFlight1BookingClass.getPrice(), passenger);
                }
                if (selectedReturnDirectFlight()) {
                    flights.add(returnDirectFlight);
                    ticket4 = new TicketEntity(returnDirectFlight, returnDirectFlightBookingClass.getName(), returnDirectFlightBookingClass.getPrice(), passenger);
                } else if (selectedReturnTransferFlight()) {
                    flights.add(returnTransferFlight1);
                    ticket5 = new TicketEntity(returnTransferFlight1, returnTransferFlight1BookingClass.getName(), returnTransferFlight1BookingClass.getPrice(), passenger);
                    flights.add(returnTransferFlight1);
                    ticket6 = new TicketEntity(returnTransferFlight1, returnTransferFlight1BookingClass.getName(), returnTransferFlight1BookingClass.getPrice(), passenger);
                }

                // add all tickets to list and set to passenger
                List<TicketEntity> tickets = new ArrayList<>();
                if (ticket1 != null) {
                    tickets.add(ticket1);
                }
                if (ticket2 != null) {
                    tickets.add(ticket2);
                }
                if (ticket3 != null) {
                    tickets.add(ticket3);
                }
                if (ticket4 != null) {
                    tickets.add(ticket4);
                }
                if (ticket5 != null) {
                    tickets.add(ticket5);
                }
                if (ticket6 != null) {
                    tickets.add(ticket6);
                }
                passenger.setTickets(tickets);

                passengers.add(passenger);
            }

        }

    }

}
