/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.distribution;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import imas.distribution.entity.TicketEntity;
import imas.distribution.sessionbean.FlightLookupSessionBeanLocal;
import imas.distribution.sessionbean.ModifyBookingSessionBeanLocal;
import imas.inventory.entity.BookingClassEntity;
import imas.inventory.entity.BookingClassRuleSetEntity;
import imas.inventory.sessionbean.CostManagementSessionBean;
import static imas.inventory.sessionbean.CostManagementSessionBean.round;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.AircraftSessionBeanLocal;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
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

    private double oldPrice;
    private double newPrice;
    private double changeFee;
    private double changeFlightPrice;
    private boolean changedFlight;
    private String approvalLinkStr;
    private String changeFlightDetails;

    private boolean upgrade;
    private double upgradePrice;
    private double upgradeBaggage;
    private double upgradeMeal;
    private double upgradeInsurance;
    private double upgradeService;
    private double upgradeFlightWifi;
    
    private double totalPriceToPay;

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
        upgrade = true;

        if (extraBaggageWeight != 0) {
            if (extraBaggageWeight != ticket.getBaggageWeight()) {
                System.out.print("extra baggage weight: " + extraBaggageWeight);
                System.out.print("actual baggage weight: " + ticket.getBaggageWeight());
                upgradeBaggage = (extraBaggageWeight - ticket.getBaggageWeight()) * 2;
                upgradePrice += upgradeBaggage;

            }
        }

        if (premiumMeal != ticket.getPremiumMeal()) {
            upgradeMeal = 50;
            upgradePrice += upgradeMeal;
        }

        if (insurance != ticket.getInsurance()) {
            upgradeInsurance = 25;
            upgradePrice += upgradeInsurance;
        }

        if (exclusiveService != ticket.getExclusiveService()) {
            upgradeService = 30;
            upgradePrice += upgradeService;
        }

        if (flightWifi != ticket.getFlightWiFi()) {
            upgradeFlightWifi = 25;
            upgradePrice += upgradeFlightWifi;
        }

        totalPriceToPay = upgradePrice + changeFlightPrice;
        totalPriceToPay = round(totalPriceToPay, 2); 
    }
    
    public void completeModifyBooking() throws IOException, PayPalRESTException {
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
        FacesContext.getCurrentInstance().getExternalContext().redirect(pay(changeFlightPrice + upgradePrice));
    }

    public void startModifyBooking() throws IOException {
        extraBaggageWeight = ticket.getBaggageWeight();
        premiumMeal = ticket.getPremiumMeal();
        insurance = ticket.getInsurance();
        exclusiveService = ticket.getExclusiveService();
        flightWifi = ticket.getFlightWiFi();
        System.out.print(extraBaggageWeight + "," + premiumMeal + "," + insurance + "," + exclusiveService + "," + flightWifi);
        changedFlight = false;
        upgrade = false;
        upgradeBaggage = 0;
        upgradeMeal = 0;
        upgradeInsurance = 0;
        upgradeService = 0;
        upgradeFlightWifi = 0;
        upgradePrice = 0;
        changeFlightPrice = 0;
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

    public String pay(double totalPrice) throws PayPalRESTException {
        String clientID = "AWvE0BAwWOfvkR-_atNy8TpEKW-Gv0-vU20BzcO6MN_gQFibDWOtUb3SCGpmjQpoYYpvru_TsIA-V_io";
        String clientSecret = "EIVHw-0paOwS1TAXrUyF8EU1VWH1ROvNIN4f6orXJZn4NNtRBCagQsokw1Mx8wsyzwR2dewdHTDEyWkR";
        System.err.println("test");

        OAuthTokenCredential tokenCredential = Payment.initConfig(new File("sdk_config.properties"));
        System.err.println("test");
//        OAuthTokenCredential tokenCredential
//                = new OAuthTokenCredential("AWvE0BAwWOfvkR-_atNy8TpEKW-Gv0-vU20BzcO6MN_gQFibDWOtUb3SCGpmjQpoYYpvru_TsIA-V_io", "EIVHw-0paOwS1TAXrUyF8EU1VWH1ROvNIN4f6orXJZn4NNtRBCagQsokw1Mx8wsyzwR2dewdHTDEyWkR");
        System.err.println("test1");
        String accessToken = tokenCredential.getAccessToken();
        //  String accessToken = new OAuthTokenCredential(clientID, clientSecret).getAccessToken();

//APIContext apiContext = new APIContext(accessToken, requestId);
//Payment payment = new Payment();
//payment.setIntent("sale");
        System.err.println("test1");
//        Address billingAddress = new Address();
//        
//        billingAddress.setLine1("52 N Main ST");
//        billingAddress.setCity("Johnstown");
//        billingAddress.setCountryCode("US");
//        billingAddress.setPostalCode("43210");
//        billingAddress.setState("OH");
        System.err.println("test2");
        Item item = new Item();
        item.setName("Merlion Airline Modify Booking");
        DecimalFormat df = new DecimalFormat("0.00");
        String priceFormat = df.format(totalPrice);
        System.out.println(priceFormat);
        item.setPrice(priceFormat);
        item.setQuantity("1");
        item.setCurrency("SGD");

        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<Item>();
        items.add(item);
        itemList.setItems(items);
//        CreditCard creditCard = new CreditCard();
//        creditCard.setNumber("4417119669820331");
//        creditCard.setType("visa");
//        creditCard.setExpireMonth(11);
//        creditCard.setExpireYear(2018);
//        creditCard.setCvv2(123);
//        creditCard.setFirstName("Joe");
//        creditCard.setLastName("Shopper");
//        creditCard.setBillingAddress(billingAddress);
        System.err.println("test3");

//        Details details = new Details();
//        details.setSubtotal("7.41");
//        details.setTax("0.03");
//        details.setShipping("0.03");
        System.err.println("test4");

        Amount amount = new Amount();

//        amount.setDetails(details);
        System.err.println("test5");
        amount.setCurrency(item.getCurrency());
        amount.setTotal(item.getPrice());

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setItemList(itemList);
        transaction.setDescription("This is the payment for Merlion Airline Modify Booking.");
        System.err.println("test6");

        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);
        System.err.println("test7");

//        FundingInstrument fundingInstrument = new FundingInstrument();
//       fundingInstrument.setCreditCard(creditCard);
//        System.err.println("test8");
//        List<FundingInstrument> fundingInstruments = new ArrayList<FundingInstrument>();
//        fundingInstruments.add(fundingInstrument);
//        System.err.println("test9");
        Payer payer = new Payer();
//        payer.setFundingInstruments(fundingInstruments);
        payer.setPaymentMethod("paypal");
        System.err.println("test10");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        System.err.println("test");
        RedirectUrls urls = new RedirectUrls();
        urls.setReturnUrl("https://localhost:8181/MerlionAirlinesExternalSystem/distribution/modifyBookingConfirmation.xhtml");
        urls.setCancelUrl("https://localhost:8181/MerlionAirlinesExternalSystem/distribution/modifyBooking.xhtml");
        payment.setRedirectUrls(urls);

//        Address billingAddress = new Address();
//        billingAddress.setLine1("52 N Main ST");
//        billingAddress.setCity("Johnstown");
//        billingAddress.setCountryCode("US");
//        billingAddress.setPostalCode("43210");
//        billingAddress.setState("OH");
//
//        CreditCard creditCard = new CreditCard();
//        creditCard.setNumber("4417119669820331");
//        creditCard.setType("visa");
//        creditCard.setExpireMonth(11);
//        creditCard.setExpireYear(2018);
//        creditCard.setCvv2(874);
//        creditCard.setFirstName("Joe");
//        creditCard.setLastName("Shopper");
//        creditCard.setBillingAddress(billingAddress);
//
//        Details amountDetails = new Details();
//        amountDetails.setTax("0.03");
//        amountDetails.setShipping("0.03");
//
//        Amount amount = new Amount();
//        amount.setTotal("7.47");
//        amount.setCurrency("USD");
//        amount.setDetails(amountDetails);
//
//        Transaction transaction = new Transaction();
//        transaction.setAmount(amount);
//        transaction.setDescription("This is the payment transaction description.");
//
//        List<Transaction> transactions = new ArrayList<Transaction>();
//        transactions.add(transaction);
//
//        FundingInstrument fundingInstrument = new FundingInstrument();
//        fundingInstrument.setCreditCard(creditCard);
//
//        List<FundingInstrument> fundingInstruments = new ArrayList<FundingInstrument>();
//        fundingInstruments.add(fundingInstrument);
//
//        Payer payer = new Payer();
//        payer.setFundingInstruments(fundingInstruments);
//        payer.setPaymentMethod("credit_card");
//
//        Payment payment = new Payment();
//        payment.setIntent("sale");
//        payment.setPayer(payer);
//        payment.setTransactions(transactions);
        Payment createdPayment = payment.create(accessToken);
        System.err.println("test12345");
        List<Links> approvalLink = createdPayment.getLinks();

        Links link = approvalLink.get(1);
        approvalLinkStr = link.getHref();

        System.err.println("getHref:" + link.getHref());
        return approvalLinkStr;
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
        TicketEntity oldTicket = ticket;
        oldPrice = ticket.getPrice();
        newPrice = selectedBookingClass.getPrice();
        BookingClassRuleSetEntity bcrs = getTicketBookingClassRuleSet(oldTicket);
        changedFlight = true;
        changeFee = changeFlightFee(oldTicket, selectedFlight, bcrs);
        changeFlightDetails = changeFlightDetails(oldTicket, selectedFlight, bcrs);
        ticket = modifyBookingSessionBean.modifyTicket(ticket, selectedFlight, selectedBookingClass);
        changeFee = round(changeFee, 2); 
        if(oldPrice > newPrice){
            changeFlightPrice = changeFee;
        }else{
            changeFlightPrice = newPrice - oldPrice + changeFee;
        }
        totalPriceToPay = upgradePrice + changeFlightPrice;
        totalPriceToPay = round(totalPriceToPay, 2); 
        FacesContext.getCurrentInstance().getExternalContext().redirect("modifyBooking.xhtml");
    }

    private BookingClassRuleSetEntity getTicketBookingClassRuleSet(TicketEntity ticket) {
        List<BookingClassEntity> flightBookingClasses = ticket.getFlight().getBookingClasses();
        for (BookingClassEntity bc : flightBookingClasses) {
            if (bc.getName().equals(ticket.getBookingClassName())) {
                return bc.getBookingClassRuleSet();
            }
        }
        return new BookingClassRuleSetEntity();
    }

    private double changeFlightFee(TicketEntity oldTicket, FlightEntity newFlight, BookingClassRuleSetEntity rule) {
        Date oldDeparture = oldTicket.getFlight().getDepartureDate();
        Date newDeparture = newFlight.getDepartureDate();
        double diffInDays = (newDeparture.getTime() - oldDeparture.getTime() / (1000 * 60 * 60 * 24.0));
        double feePercent;
        if (diffInDays > 60) {
            feePercent = rule.getChangeFlightFeeForMoreThan60Days();
        } else {
            feePercent = rule.getChangeFlightFeeForLessThan60Days();
        }
        return feePercent * oldTicket.getPrice();
    }

    private String changeFlightDetails(TicketEntity oldTicket, FlightEntity newFlight, BookingClassRuleSetEntity rule) {
        Date oldDeparture = oldTicket.getFlight().getDepartureDate();
        Date newDeparture = newFlight.getDepartureDate();
        double diffInDays = (newDeparture.getTime() - oldDeparture.getTime() / (1000 * 60 * 60 * 24.0));
        String changeFlight = "Change flight ";
        if (diffInDays > 60) {
            Double changeFeePercent = 100 * rule.getChangeFlightFeeForMoreThan60Days();
            changeFlight = changeFlight + "before 60 days to departure: " + changeFeePercent.toString() + "%";
        } else {
            Double changeFeePercent = 100 * rule.getChangeFlightFeeForLessThan60Days();
            changeFlight = changeFlight + "less than 60 days to departure: " + changeFeePercent.toString() + "%";
        }
        return changeFlight;
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

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public double getChangeFee() {
        return changeFee;
    }

    public void setChangeFee(double changeFee) {
        this.changeFee = changeFee;
    }

    public boolean isChangedFlight() {
        return changedFlight;
    }

    public void setChangedFlight(boolean changedFlight) {
        this.changedFlight = changedFlight;
    }

    public String getChangeFlightDetails() {
        return changeFlightDetails;
    }

    public void setChangeFlightDetails(String changeFlightDetails) {
        this.changeFlightDetails = changeFlightDetails;
    }

    public boolean isUpgrade() {
        return upgrade;
    }

    public void setUpgrade(boolean upgrade) {
        this.upgrade = upgrade;
    }

    public double getUpgradePrice() {
        return upgradePrice;
    }

    public void setUpgradePrice(double upgradePrice) {
        this.upgradePrice = upgradePrice;
    }

    public double getUpgradeBaggage() {
        return upgradeBaggage;
    }

    public void setUpgradeBaggage(double upgradeBaggage) {
        this.upgradeBaggage = upgradeBaggage;
    }

    public double getUpgradeMeal() {
        return upgradeMeal;
    }

    public void setUpgradeMeal(double upgradeMeal) {
        this.upgradeMeal = upgradeMeal;
    }

    public double getUpgradeInsurance() {
        return upgradeInsurance;
    }

    public void setUpgradeInsurance(double upgradeInsurance) {
        this.upgradeInsurance = upgradeInsurance;
    }

    public double getUpgradeService() {
        return upgradeService;
    }

    public void setUpgradeService(double upgradeService) {
        this.upgradeService = upgradeService;
    }

    public double getUpgradeFlightWifi() {
        return upgradeFlightWifi;
    }

    public void setUpgradeFlightWifi(double upgradeFlightWifi) {
        this.upgradeFlightWifi = upgradeFlightWifi;
    }

    public double getChangeFlightPrice() {
        return changeFlightPrice;
    }

    public void setChangeFlightPrice(double changeFlightPrice) {
        this.changeFlightPrice = changeFlightPrice;
    }

    public double getTotalPriceToPay() {
        return totalPriceToPay;
    }

    public void setTotalPriceToPay(double totalPriceToPay) {
        this.totalPriceToPay = totalPriceToPay;
    }
}
