/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.distribution;

import imas.distribution.entity.TicketEntity;
import imas.distribution.sessionbean.FlightLookupSessionBeanLocal;
import imas.distribution.sessionbean.ModifyBookingSessionBeanLocal;
import imas.inventory.entity.BookingClassEntity;
import imas.inventory.entity.BookingClassRuleSetEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.AircraftSessionBeanLocal;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author Howard
 */
@ManagedBean
@SessionScoped
public class ModifyBookingManagedBean {

    @EJB
    private AircraftSessionBeanLocal aircraftSessionBean;

    @EJB
    private FlightLookupSessionBeanLocal flightLookupSessionBean;

    @EJB
    private ModifyBookingSessionBeanLocal modifyBookingSessionBean;

    private String referenceNumber;
    private FlightEntity flight;
    private TicketEntity ticket;
    private List<TicketEntity> tickets;
    private String passportNumber;
    private double extraBaggageWeight;
    private boolean premiumMeal;
    private boolean insurance;
    private boolean exclusiveService;
    private boolean flightWifi;
    private double extraPrice;

    private AirportEntity originAirport;
    private AirportEntity destinationAirport;
    private Date departureDate;
    private String seatClass;
    private Date departureMinDate;
    private List<AirportEntity> airportList;
    private List<SelectItem> originAirportsByCountry;
    private List<SelectItem> destinationAirportsByCountry;
    private FlightEntity selectedFlight;
    private BookingClassEntity selectedBookingClass;
    private RouteEntity directRoute;
    private int activeIndex;
    private boolean tab1Disabled;
    private boolean tab2Disabled;
    private boolean tab3Disabled;
    private LineChartModel flight7DayPricing; // 7 day lowest price
    private List<FlightEntity> flightCandidates;
    private List<BookingClassEntity> bookingClassCandidates;

    /**
     * Creates a new instance of ModifyBookingManagedBean
     */
    public ModifyBookingManagedBean() {
    }

    @PostConstruct
    public void init() {
    }

    public void seachTicket() throws IOException {
        tickets = modifyBookingSessionBean.getTicketList(referenceNumber, passportNumber);
        if (tickets == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Sorry, the reference number or passport number is invalid or the flight has expired", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ticketList", tickets);
            FacesContext.getCurrentInstance().getExternalContext().redirect("modifyBookingTicketResult.xhtml");
        }
    }

    public void upgradePremiumService() {
        System.out.print(extraBaggageWeight + "," + premiumMeal + "," + insurance + "," + exclusiveService + "," + flightWifi);
    }

    public void completeModifyBooking() throws IOException {
        ticket.setBaggageWeight(extraBaggageWeight);
        ticket.setPremiumMeal(premiumMeal);
        ticket.setInsurance(insurance);
        ticket.setExclusiveService(exclusiveService);
        ticket.setFlightWiFi(flightWifi);

        modifyBookingSessionBean.flushModification(ticket);
        referenceNumber = null;
        passportNumber = null;
        flight = null;
        tickets = null;
        ticket = null;
        FacesContext.getCurrentInstance().getExternalContext().redirect("https://localhost:8181/MerlionAirlinesExternalSystem/index.xhtml");
    }

    public void startModifyBooking() throws IOException {
        extraBaggageWeight = ticket.getBaggageWeight();
        premiumMeal = ticket.getPremiumMeal();
        insurance = ticket.getInsurance();
        exclusiveService = ticket.getExclusiveService();
        flightWifi = ticket.getFlightWiFi();
        System.out.print(extraBaggageWeight + "," + premiumMeal + "," + insurance + "," + exclusiveService + "," + flightWifi);

        FacesContext.getCurrentInstance().getExternalContext().redirect("modifyBooking.xhtml");

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

    public void selectNewFlight() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("selectNewFlight.xhtml");
        fetchAllAirports();
        airportList = aircraftSessionBean.getAirports();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("airportList", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("airportList", airportList);

        Calendar today = Calendar.getInstance();
        departureMinDate = today.getTime();
        today.add(Calendar.YEAR, 1);
        activeIndex = 0;
        tab1Disabled = false;
        tab2Disabled = true;
        tab3Disabled = true;

        flight7DayPricing = new LineChartModel();
        departureDate = flightLookupSessionBean.getDateAfterDays(new Date(), 60);
    }

    public void onOriginChange() {
        if (originAirport != null) {
            updateDestinationAirportList();
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

                if (airport.equals(originAirport) || (!flightLookupSessionBean.reachableDirectly(originAirport, airport))) {
                    selectItem.setDisabled(true);
                }

                selectItems[i] = selectItem;
            }
            group.setSelectItems(selectItems);
            destinationAirportsByCountry.add(group);
        }
    }

    public void searchFlight() {
        initSelectFlight();
        init7DayPricing();
        tab2Disabled = false;
        activeIndex = 1;
    }

    public void initSelectFlight() {

        selectedFlight = null;
        // load departure flight data
        directRoute = flightLookupSessionBean.getDirectRoute(originAirport, destinationAirport);
        if (directRoute != null) {
            flightCandidates = flightLookupSessionBean.getAvailableFlights(directRoute, departureDate,
                    flightLookupSessionBean.getDateAfterDays(departureDate, 1));
        }
        tab2Disabled = false;

    }

    public void init7DayPricing() {

        flight7DayPricing = new LineChartModel();
        int totalPurchaseAmount = 1;
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
        flight7DayPricing.addSeries(departure7DayFlightPriceData);
        flight7DayPricing.setTitle("Lowest Fare for Direct Flight " + directRoute + " in 7 Days");
        flight7DayPricing.setLegendPosition("s");
        flight7DayPricing.setShowPointLabels(false);
        flight7DayPricing.getAxes().put(AxisType.X, new CategoryAxis("Date"));
        Axis departureYAxis = flight7DayPricing.getAxis(AxisType.Y);
        departureYAxis.setLabel("Price (S$)");
        if (!departureLowestFares.isEmpty()) {
            Collections.sort(departureLowestFares);
            int minDepartureLowestFare = departureLowestFares.get(0);
            int maxDepartureLowestFare = departureLowestFares.get(departureLowestFares.size() - 1);
            departureYAxis.setMin((int) minDepartureLowestFare * 0.95);
            departureYAxis.setMax((int) maxDepartureLowestFare * 1.05);
        }
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
                    bcs = filterNonAgencyBookingClasses(bcs);
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

    private List<BookingClassEntity> filterNonAgencyBookingClasses(List<BookingClassEntity> originalBCList) {
        List<BookingClassEntity> nonAgencyBCList = new ArrayList<>();
        for (BookingClassEntity bc : originalBCList) {
            if (!bc.isAgencyBookingClass()) {
                nonAgencyBCList.add(bc);
            }
        }
        return nonAgencyBCList;
    }

    public boolean flightSelectionDisabled(FlightEntity flight) {
        boolean flag = true;
        int totalPurchaseNo = 1;

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

    public String getDate(Date date) {
        DateFormat df = new SimpleDateFormat("MMM d, yyyy");
        return df.format(date);
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
    private int getLowestFare(FlightEntity flight) {
        int totalPurchaseNo = 1;
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

    public void submitFlightsToSelectBookingClasses() {
        if (selectedFlight != null) {
            initSelectBookingClass();
            activeIndex = 2;
            tab3Disabled = false;
        } else {
            activeIndex = 1;
            tab3Disabled = true;
        }
    }

    public void initSelectBookingClass() {

        selectedBookingClass = null;

        int totalPurchaseNo = 1;
        List<BookingClassEntity> bookingClassList
                = flightLookupSessionBean.getAvailableBookingClassUnderFlightUnderSeatClass(selectedFlight, seatClass, totalPurchaseNo);
        bookingClassCandidates = filterNonAgencyBookingClasses(bookingClassList);

    }

    public boolean bookingClassSelectionDisabled(BookingClassEntity bc) {
        return (flightLookupSessionBean.getQuotaLeft(bc) < 1);
    }

    public void showBCRules(BookingClassEntity bookingClass) {
        BookingClassRuleSetEntity bookingClassRuleSet = bookingClass.getBookingClassRuleSet();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bookingClass.getName(), bookingClassRuleSet.toString());
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
    
    public void submitBookingClass() throws IOException {
        ticket = modifyBookingSessionBean.modifyTicket(ticket, selectedFlight, selectedBookingClass);
        FacesContext.getCurrentInstance().getExternalContext().redirect("modifyBooking.xhtml");
    }
    
    public RouteEntity getDirectRoute() {
        return directRoute;
    }

    public void setDirectRoute(RouteEntity directRoute) {
        this.directRoute = directRoute;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public TicketEntity getTicket() {
        return ticket;
    }

    public void setTicket(TicketEntity ticket) {
        this.ticket = ticket;
    }

    public List<TicketEntity> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketEntity> tickets) {
        this.tickets = tickets;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public double getExtraBaggageWeight() {
        return extraBaggageWeight;
    }

    public void setExtraBaggageWeight(double extraBaggageWeight) {
        this.extraBaggageWeight = extraBaggageWeight;
    }

    public boolean isPremiumMeal() {
        return premiumMeal;
    }

    public void setPremiumMeal(boolean premiumMeal) {
        this.premiumMeal = premiumMeal;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    public boolean isExclusiveService() {
        return exclusiveService;
    }

    public void setExclusiveService(boolean exclusiveService) {
        this.exclusiveService = exclusiveService;
    }

    public boolean isFlightWifi() {
        return flightWifi;
    }

    public void setFlightWifi(boolean flightWifi) {
        this.flightWifi = flightWifi;
    }

    public double getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(double extraPrice) {
        this.extraPrice = extraPrice;
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

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public Date getDepartureMinDate() {
        return departureMinDate;
    }

    public void setDepartureMinDate(Date departureMinDate) {
        this.departureMinDate = departureMinDate;
    }

    public List<AirportEntity> getAirportList() {
        return airportList;
    }

    public void setAirportList(List<AirportEntity> airportList) {
        this.airportList = airportList;
    }

    public List<SelectItem> getOriginAirportsByCountry() {
        return originAirportsByCountry;
    }

    public void setOriginAirportsByCountry(List<SelectItem> originAirportsByCountry) {
        this.originAirportsByCountry = originAirportsByCountry;
    }

    public List<SelectItem> getDestinationAirportsByCountry() {
        return destinationAirportsByCountry;
    }

    public void setDestinationAirportsByCountry(List<SelectItem> destinationAirportsByCountry) {
        this.destinationAirportsByCountry = destinationAirportsByCountry;
    }

    public FlightEntity getSelectedFlight() {
        return selectedFlight;
    }

    public void setSelectedFlight(FlightEntity selectedFlight) {
        this.selectedFlight = selectedFlight;
    }

    public BookingClassEntity getSelectedBookingClass() {
        return selectedBookingClass;
    }

    public void setSelectedBookingClass(BookingClassEntity selectedBookingClass) {
        this.selectedBookingClass = selectedBookingClass;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
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

    public LineChartModel getFlight7DayPricing() {
        return flight7DayPricing;
    }

    public void setFlight7DayPricing(LineChartModel flight7DayPricing) {
        this.flight7DayPricing = flight7DayPricing;
    }

    public List<FlightEntity> getFlightCandidates() {
        return flightCandidates;
    }

    public void setFlightCandidates(List<FlightEntity> flightCandidates) {
        this.flightCandidates = flightCandidates;
    }

    public List<BookingClassEntity> getBookingClassCandidates() {
        return bookingClassCandidates;
    }

    public void setBookingClassCandidates(List<BookingClassEntity> bookingClassCandidates) {
        this.bookingClassCandidates = bookingClassCandidates;
    }
}
