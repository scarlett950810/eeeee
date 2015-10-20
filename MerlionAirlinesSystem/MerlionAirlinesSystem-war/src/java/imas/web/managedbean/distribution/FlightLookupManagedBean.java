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
import imas.inventory.entity.BookingClassRuleSetEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.AircraftSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.persistence.PostRemove;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author Scarlett
 */
@ManagedBean
@SessionScoped
public class FlightLookupManagedBean implements Serializable {

    @EJB
    private FlightLookupSessionBeanLocal flightLookupSessionBean;
    
    @EJB
    private AircraftSessionBeanLocal aircraftSessionBean;

    private AirportEntity originAirport;
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
    private List<SelectItem> originAirportsByCountry;
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
    private LineChartModel departure7DayPricing; // 7 day lowest price for departure direct flight
    private LineChartModel return7DayPricing; // 7 day lowest price for return direct flight

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

    List<PassengerEntity> passengers = new ArrayList<>();
    List<FlightEntity> flights = new ArrayList<>();

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

        departure7DayPricing = new LineChartModel();
        return7DayPricing = new LineChartModel();
        departureDate = flightLookupSessionBean.getDateAfterDays(new Date(), 60);
        returnDate = flightLookupSessionBean.getDateAfterDays(departureDate, 7);
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

    public AirportEntity getOriginAirport() {
        return originAirport;
    }

    public void setOriginAirport(AirportEntity originAirport) {
        this.originAirport = originAirport;
    }

    public AirportEntity getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(AirportEntity destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public List<SelectItem> getOriginAirportsByCountry() {
        return originAirportsByCountry;
    }

    public void setOriginAirportsByCountry(List<SelectItem> originAirportsByCountry) {
        this.originAirportsByCountry = originAirportsByCountry;
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

    public LineChartModel getDeparture7DayPricing() {
        return departure7DayPricing;
    }

    public void setDeparture7DayPricing(LineChartModel departure7DayPricing) {
        this.departure7DayPricing = departure7DayPricing;
    }

    public LineChartModel getReturn7DayPricing() {
        return return7DayPricing;
    }

    public void setReturn7DayPricing(LineChartModel return7DayPricing) {
        this.return7DayPricing = return7DayPricing;
    }

    public String getUserFriendlyTime(double hours) {
        int hourNo = (int) hours;
        int minNo = (int) (60 * (hours - hourNo));
        return hourNo + " hour " + minNo + " mins";
    }

    public String getLowestFareString(FlightEntity flight) {

        if (getLowestFare(flight) != -1) {
            return "S$" + getLowestFare(flight);
        } else {
            return "Not enough quota left";
        }

    }

    // return lowest fare unlessnot enough quota then return -1
    public int getLowestFare(FlightEntity flight) {
        int totalPurchaseNo = adultNo + childNo + infantNo;
        List<BookingClassEntity> bcs = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(flight, seatClass, totalPurchaseNo);

        double lowestFare = Double.MAX_VALUE;
        for (BookingClassEntity bc : bcs) {
            if (bc.getPrice() < lowestFare) {
                lowestFare = bc.getPrice();
            }
        }
        if (!flightSelectionDisabled(flight)) {
            int lowestFareInt = (int) lowestFare;
            return lowestFareInt;
        } else {
            return -1;
        }

    }

    public boolean flightSelectionDisabled(FlightEntity flight) {
        boolean flag = true;
        int totalPurchaseNo = adultNo + childNo + infantNo;

        List<BookingClassEntity> bcs = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(flight, seatClass, totalPurchaseNo);

        if (seatClass.equals("Economy Class")) {
            for (BookingClassEntity bc : bcs) {
                if (flightLookupSessionBean.getQuotaLeft(bc) >= totalPurchaseNo) {
                    flag = false;
                }
            }
        } else {
            if (bcs.size() > 0) {
                flag = false;
            }
        }

        return flag;
    }

    private void fetchAllAirports() {
        originAirportsByCountry = new ArrayList<>();
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
            originAirportsByCountry.add(group);
        }

        destinationAirportsByCountry = originAirportsByCountry;
    }

    private void updateOriginAirportList() {

        originAirportsByCountry = new ArrayList<>();
        List<String> countries = flightLookupSessionBean.getAllCountryNames();
        for (String country : countries) {
            SelectItemGroup group = new SelectItemGroup(country);
            List<AirportEntity> airportsInCountry = flightLookupSessionBean.getAllAirportsInCountry(country);

            SelectItem[] selectItems = new SelectItem[airportsInCountry.size()];

            for (int i = 0; i < airportsInCountry.size(); i++) {
                AirportEntity airport = airportsInCountry.get(i);
                SelectItem selectItem = new SelectItem(airport, airport.toString());

                if (airport.equals(destinationAirport) || (!flightLookupSessionBean.reachable(destinationAirport, airport))) {
                    selectItem.setDisabled(true);
                }

                selectItems[i] = selectItem;
            }
            group.setSelectItems(selectItems);
            originAirportsByCountry.add(group);

        }

    }

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

                if (airport.equals(originAirport) || (!flightLookupSessionBean.reachable(originAirport, airport))) {
                    selectItem.setDisabled(true);
                }

                selectItems[i] = selectItem;
            }
            group.setSelectItems(selectItems);
            destinationAirportsByCountry.add(group);

        }

    }

    public void onOriginChange() {
        if (originAirport != null) {
            updateDestinationAirportList();
        }
    }

    public void onDestinationChange() {
        if (destinationAirport != null) {
            updateOriginAirportList();
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
        init7DayPricing();
        tab2Disabled = false;
        activeIndex = 1;
    }

    public void searchFromHomePage() throws IOException {
        initSelectFlight();
        init7DayPricing();
        activeIndex = 1;
        tab2Disabled = false;
        FacesContext.getCurrentInstance().getExternalContext().redirect("selectFlight.xhtml");
    }

    public void initSelectFlight() {

        departureDirectFlight = null;
        departureTransferFlight1 = null;
        departureTransferFlight2 = null;
        returnDirectFlight = null;
        returnTransferFlight1 = null;
        returnTransferFlight2 = null;

        // load departure flight data
        departureDirectRoute
                = flightLookupSessionBean.getDirectRoute(originAirport, destinationAirport);
        if (departureDirectRoute != null) {
            departureDirectFlightCandidates
                    = flightLookupSessionBean.getAvailableFlights(departureDirectRoute, departureDate, flightLookupSessionBean.getDateAfterDays(departureDate, 1));
            departureHasDirectFlight = (departureDirectFlightCandidates.size() > 0);
        } else {
            departureHasDirectFlight = false;
        }
        departureTransferFlightCandidates = flightLookupSessionBean.getTransferRoutes(originAirport, destinationAirport, departureDate);
        departureHasTransferFlight = (departureTransferFlightCandidates.size() > 0);

        if (twoWay) {
            // loading return flight data
            returnDirectRoute
                    = flightLookupSessionBean.getDirectRoute(destinationAirport, originAirport);
            if (returnDirectRoute != null) {
                returnDirectFlightCandidates
                        = flightLookupSessionBean.getAvailableFlights(returnDirectRoute, returnDate, flightLookupSessionBean.getDateAfterDays(returnDate, 1));
                returnHasDirectFlight = (returnDirectFlightCandidates.size() > 0);
            } else {
                returnHasDirectFlight = false;
            }
            returnTransferFlightCandidates = flightLookupSessionBean.getTransferRoutes(originAirport, destinationAirport, returnDate);
            returnHasTransferFlight = (returnTransferFlightCandidates.size() > 0);

        }
        showTransferOptions = true;
        tab2Disabled = false;

    }

    public boolean flightsAvailableOnDate(AirportEntity flightsAvailableOnDate_originAirport, AirportEntity flightsAvailableOnDate_destinationAirport,
            Date flightsAvailableOnDate_date, String seatClass, int totalPurchaseNo) {
        RouteEntity flightsAvailableOnDate_directRoute;
        List<FlightEntity> flightsAvailableOnDate_directFlightCandidates;
        boolean flightsAvailableOnDate_hasAvailableDirectFlight = false;

        // direct flight data
        flightsAvailableOnDate_directRoute
                = flightLookupSessionBean.getDirectRoute(flightsAvailableOnDate_originAirport, flightsAvailableOnDate_destinationAirport);
        if (flightsAvailableOnDate_directRoute != null) {
            flightsAvailableOnDate_directFlightCandidates
                    = flightLookupSessionBean.getAvailableFlights(flightsAvailableOnDate_directRoute, flightsAvailableOnDate_date, flightLookupSessionBean.getDateAfterDays(flightsAvailableOnDate_date, 1));
            if (flightsAvailableOnDate_directFlightCandidates.size() > 0) {
                for (FlightEntity flight : flightsAvailableOnDate_directFlightCandidates) {
                    // check if there is a single booking class in the particular flight fulfill the booking amount
                    List<BookingClassEntity> bcs
                            = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(flight, seatClass, totalPurchaseNo);
                    if (seatClass.equals("Economy Class")) {
                        for (BookingClassEntity bc : bcs) {
                            if (flightLookupSessionBean.getQuotaLeft(bc) >= totalPurchaseNo) {
                                flightsAvailableOnDate_hasAvailableDirectFlight = true;
                            }
                        }
                    } else {
                        if (bcs.size() > 0) {
                            flightsAvailableOnDate_hasAvailableDirectFlight = true;
                        }
                    }
                }
            }
        } else {
            flightsAvailableOnDate_hasAvailableDirectFlight = false;
        }

        return flightsAvailableOnDate_hasAvailableDirectFlight;
    }

    public int lowestFareOnDate(AirportEntity flightsAvailableOnDate_originAirport, AirportEntity flightsAvailableOnDate_destinationAirport,
            Date flightsAvailableOnDate_date, String seatClass, int totalPurchaseNo) {
        RouteEntity flightsAvailableOnDate_directRoute;
        List<FlightEntity> flightsAvailableOnDate_directFlightCandidates;
        int flightsAvailableOnDate_LowestFare = Integer.MAX_VALUE;

        // direct flight data
        flightsAvailableOnDate_directRoute
                = flightLookupSessionBean.getDirectRoute(flightsAvailableOnDate_originAirport, flightsAvailableOnDate_destinationAirport);
        if (flightsAvailableOnDate_directRoute != null) {
            flightsAvailableOnDate_directFlightCandidates
                    = flightLookupSessionBean.getAvailableFlights(flightsAvailableOnDate_directRoute, flightsAvailableOnDate_date, flightLookupSessionBean.getDateAfterDays(flightsAvailableOnDate_date, 1));
            if (flightsAvailableOnDate_directFlightCandidates.size() > 0) {
                for (FlightEntity flight : flightsAvailableOnDate_directFlightCandidates) {
                    // check if there is a single booking class in the particular flight fulfill the booking amount
                    List<BookingClassEntity> bcs
                            = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(flight, seatClass, totalPurchaseNo);
                    if (seatClass.equals("Economy Class")) {
                        for (BookingClassEntity bc : bcs) {
                            if (flightLookupSessionBean.getQuotaLeft(bc) >= totalPurchaseNo && bc.getPrice() < flightsAvailableOnDate_LowestFare) {
                                double price = bc.getPrice();
                                flightsAvailableOnDate_LowestFare = (int) price;
                            }
                        }
                    } else {
                        if (bcs.size() > 0) {
                            for (BookingClassEntity bc : bcs) {
                                if (bc.getPrice() < flightsAvailableOnDate_LowestFare) {
                                    double price = bc.getPrice();
                                    flightsAvailableOnDate_LowestFare = (int) price;
                                }
                            }
                        }
                    }
                }
            }
        }

        return flightsAvailableOnDate_LowestFare;
    }

    public void init7DayPricing() {

        departure7DayPricing = new LineChartModel();
        return7DayPricing = new LineChartModel();

        int totalPurchaseAmount = adultNo + childNo + infantNo;

        ArrayList<Integer> departureLowestFares = new ArrayList<>();
        ChartSeries departure7DayFlightPriceData = new ChartSeries();
        departure7DayFlightPriceData.setLabel("Lowest Fare");
        for (int i = -3; i < 4; i++) {
            Date date = flightLookupSessionBean.getDateAfterDays(departureDate, i);
            if (flightsAvailableOnDate(originAirport, destinationAirport, date, seatClass, totalPurchaseAmount)) {
                int lowestFare = lowestFareOnDate(originAirport, destinationAirport, date, seatClass, totalPurchaseAmount);
                departure7DayFlightPriceData.set(getDate(date), lowestFare);
                departureLowestFares.add(lowestFare);
            } else {
                departure7DayFlightPriceData.set(getDate(date), null);
            }
        }
        departure7DayPricing.addSeries(departure7DayFlightPriceData);
        departure7DayPricing.setTitle("Lowest Fare for Direct Flight " + departureDirectRoute + " in 7 Days");
        departure7DayPricing.setLegendPosition("s");
        departure7DayPricing.setShowPointLabels(false);
        departure7DayPricing.getAxes().put(AxisType.X, new CategoryAxis("Date"));
        Axis departureYAxis = departure7DayPricing.getAxis(AxisType.Y);
        departureYAxis.setLabel("Price (S$)");
        if (!departureLowestFares.isEmpty()) {
            Collections.sort(departureLowestFares);
            int minDepartureLowestFare = departureLowestFares.get(0);
            int maxDepartureLowestFare = departureLowestFares.get(departureLowestFares.size() - 1);
            departureYAxis.setMin((int) minDepartureLowestFare * 0.95);
            departureYAxis.setMax((int) maxDepartureLowestFare * 1.05);
        }

        if (twoWay) {
            ArrayList<Integer> returnLowestFares = new ArrayList<>();
            ChartSeries return7DayFlightPriceData = new ChartSeries();
            return7DayFlightPriceData.setLabel("Lowest Fare");
            for (int i = -3; i < 4; i++) {
                Date date = flightLookupSessionBean.getDateAfterDays(returnDate, i);
                if (flightsAvailableOnDate(destinationAirport, originAirport, date, seatClass, totalPurchaseAmount)) {
                    int lowestFare = lowestFareOnDate(destinationAirport, originAirport, date, seatClass, totalPurchaseAmount);
                    return7DayFlightPriceData.set(getDate(date), lowestFare);
                    returnLowestFares.add(lowestFare);
                } else {
                    return7DayFlightPriceData.set(getDate(date), null);
                }
            }
            return7DayPricing.addSeries(return7DayFlightPriceData);
            return7DayPricing.setTitle("Lowest Fare for Return Flight " + returnDirectRoute + " in 7 Days");
            return7DayPricing.setLegendPosition("s");
            return7DayPricing.setShowPointLabels(false);
            return7DayPricing.getAxes().put(AxisType.X, new CategoryAxis("Date"));
            Axis returnYAxis = return7DayPricing.getAxis(AxisType.Y);
            returnYAxis.setLabel("Price (S$)");
            if (!returnLowestFares.isEmpty()) {
                Collections.sort(returnLowestFares);
                int minReturnLowestFare = returnLowestFares.get(0);
                int maxReturnLowestFare = returnLowestFares.get(returnLowestFares.size() - 1);
                returnYAxis.setMin((int) minReturnLowestFare * 0.95);
                returnYAxis.setMax((int) maxReturnLowestFare * 1.05);
            }

        }

    }

    public String getDate(Date date) {
        DateFormat df = new SimpleDateFormat("MMM d, yyyy");
        return df.format(date);
    }

    public void select7DayPricing(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onDepartureDirectFlightSelect(SelectEvent event) {
        departureTransferFlight1 = null;
        departureTransferFlight2 = null;
    }

    public void onDepartureTransferFlightSelect(SelectEvent event) {
        departureDirectFlight = null;
    }

    public void onReturnDirectFlightSelect(SelectEvent event) {
        returnTransferFlight1 = null;
        returnTransferFlight2 = null;
    }

    public void onReturnTransferFlightSelect(SelectEvent event) {
        returnDirectFlight = null;
    }

    public boolean selectedDepartureDirectFlight() {
        return departureDirectFlight != null && departureTransferFlight1 == null && departureTransferFlight2 == null;
    }

    public boolean selectedDepartureTransferFlight() {
        return departureDirectFlight == null && departureTransferFlight1 != null && departureTransferFlight2 != null;
    }

    public boolean selectedDepartureTransferFlightTimeCheck() {

        if (departureTransferFlight1.getArrivalDate().after(departureTransferFlight2.getDepartureDate())) {
            return false;
        } else {
            long diff = departureTransferFlight1.getArrivalDate().getTime() - departureTransferFlight2.getDepartureDate().getTime();
            long diffInHour = TimeUnit.MILLISECONDS.toHours(diff);
            return (diffInHour > 3);
        }

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

    public boolean selectedReturnTransferFlightTimeCheck() {
        if (returnTransferFlight1.getArrivalDate().after(returnTransferFlight2.getDepartureDate())) {
            return false;
        } else {
            long diff = returnTransferFlight1.getArrivalDate().getTime() - returnTransferFlight2.getDepartureDate().getTime();
            long diffInHour = TimeUnit.MILLISECONDS.toHours(diff);
            return (diffInHour > 3);
        }

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
        } else if (selectedDepartureTransferFlight()) {
            if (!selectedDepartureTransferFlightTimeCheck()) {
                flag = false;
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "For departure transfer flights, "
                                + "please make sure the first one arrives before the second one departure, and leave at least 3 hours in between.", ""));
            }
        }

        if (twoWay) {
            if (!selectedReturnDirectFlight() && !selectedReturnTransferFlight() && !selectedNoReturnFlights()) {
                flag = false;
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "For return, please select one direct flight or a pair of transfer flights", ""));
            } else if (selectedReturnTransferFlight()) {
                if (!selectedReturnTransferFlightTimeCheck()) {
                    flag = false;
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "For return transfer flights, "
                                    + "please make sure the first one arrives before the second one departure, leave at least 3 hours in between.", ""));
                }
            }
        }
        return flag;
    }

    public void initSelectBookingClass() {
        
        departureDirectFlightBookingClass = null;
        departureTransferFlight1BookingClass = null;
        departureTransferFlight2BookingClass = null;
        returnDirectFlightBookingClass = null;
        returnTransferFlight1BookingClass = null;
        returnTransferFlight2BookingClass = null;

        int totalPurchaseNo = adultNo + childNo + infantNo;
        List<List<BookingClassEntity>> bookingClassLists = new ArrayList<>();

        if (selectedDepartureDirectFlight()) {
            List<BookingClassEntity> bookingClassList
                    = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(departureDirectFlight, seatClass, totalPurchaseNo);
            departureDirectFlightBookingClassCandidates = bookingClassList;
        } else if (selectedDepartureTransferFlight()) {
            List<BookingClassEntity> bookingClassList1
                    = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(departureTransferFlight1, seatClass, totalPurchaseNo);
            List<BookingClassEntity> bookingClassList2
                    = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(departureTransferFlight2, seatClass, totalPurchaseNo);
            departureTransferFlight1BookingClassCandidates = bookingClassList1;
            departureTransferFlight2BookingClassCandidates = bookingClassList2;
        }

        if (twoWay) {
            if (selectedReturnDirectFlight()) {
                List<BookingClassEntity> bookingClassList
                        = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(returnDirectFlight, seatClass, totalPurchaseNo);
                returnDirectFlightBookingClassCandidates = bookingClassList;
            } else if (selectedReturnTransferFlight()) {
                List<BookingClassEntity> bookingClassList1
                        = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(returnTransferFlight1, seatClass, totalPurchaseNo);
                List<BookingClassEntity> bookingClassList2
                        = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(returnTransferFlight2, seatClass, totalPurchaseNo);
                returnTransferFlight1BookingClassCandidates = bookingClassList1;
                returnTransferFlight2BookingClassCandidates = bookingClassList2;
            }
        }

    }

    public boolean bookingClassSelectionDisabled(BookingClassEntity bc) {
        int totalPurchaseNo = adultNo + childNo + infantNo;
        return (flightLookupSessionBean.getQuotaLeft(bc) < totalPurchaseNo);
    }
    
    public BookingClassRuleSetEntity getBookingClassRule(FlightEntity flight, BookingClassEntity bookingClass) {
        List<BookingClassRuleSetEntity> bcrss = (List<BookingClassRuleSetEntity>) flight.getBookingClassRuleSetEntities();
        for (BookingClassRuleSetEntity bcrs: bcrss) {
            if (bcrs.getBookingClass().equals(bookingClass)) {
                return bcrs;
            }
        }
        return null;
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

            if (selectedDepartureDirectFlight()) {
                flights.add(departureDirectFlight);
            } else if (selectedDepartureTransferFlight()) {
                flights.add(departureTransferFlight1);
                flights.add(departureTransferFlight1);
            }
            if (selectedReturnDirectFlight()) {
                flights.add(returnDirectFlight);
            } else if (selectedReturnTransferFlight()) {
                flights.add(returnTransferFlight1);
                flights.add(returnTransferFlight1);
            }

            for (int i = 0; i < adultNo + childNo + infantNo; i++) {
                PassengerEntity passenger = new PassengerEntity();
                TicketEntity ticket1 = null, ticket2 = null, ticket3 = null, ticket4 = null, ticket5 = null, ticket6 = null;
                if (selectedDepartureDirectFlight()) {
                    ticket1 = new TicketEntity(departureDirectFlight, departureDirectFlightBookingClass.getName(), departureDirectFlightBookingClass.getPrice(), passenger);
                } else if (selectedDepartureTransferFlight()) {
                    ticket2 = new TicketEntity(departureTransferFlight1, departureTransferFlight1BookingClass.getName(), departureTransferFlight1BookingClass.getPrice(), passenger);
                    ticket3 = new TicketEntity(departureTransferFlight1, departureTransferFlight1BookingClass.getName(), departureTransferFlight1BookingClass.getPrice(), passenger);
                }
                if (selectedReturnDirectFlight()) {
                    ticket4 = new TicketEntity(returnDirectFlight, returnDirectFlightBookingClass.getName(), returnDirectFlightBookingClass.getPrice(), passenger);
                } else if (selectedReturnTransferFlight()) {
                    ticket5 = new TicketEntity(returnTransferFlight1, returnTransferFlight1BookingClass.getName(), returnTransferFlight1BookingClass.getPrice(), passenger);
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
