/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.web.managedbean;

import GDS.entity.GDSAirportEntity;
import GDS.entity.GDSBookingClassEntity;
import GDS.entity.GDSFlightEntity;
import GDS.sessionbean.GDSAirportSessionBeanLocal;
import GDS.sessionbean.GDSFlightSessionBeanLocal;
import GDS.sessionbean.GDSTransferFlight;
import imas.distribution.sessionbean.MakeBookingSessionBeanLocal;
import imas.inventory.sessionbean.CostManagementSessionBean;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Scarlett
 */
@ManagedBean
@SessionScoped
public class GDSFlightLookupManagedBean implements Serializable {

    @EJB
    private GDSFlightSessionBeanLocal gDSFlightSessionBean;

    @EJB
    private GDSAirportSessionBeanLocal gDSAirportSessionBean;

    @EJB
    private MakeBookingSessionBeanLocal makeBookingSessionBean;

    // for displaying search criteria
    private GDSAirportEntity originAirport;
    private GDSAirportEntity destinationAirport;
    private boolean twoWay;
    private Date departureDate;
    private Date returnDate;
    private int adultNo;
    private int childNo;
    private int infantNo;
    private Date departureMinDate;
    private Date returnMinDate;
    private List<GDSAirportEntity> airportList;
    private List<SelectItem> originAirportsByCountry;
    private List<SelectItem> destinationAirportsByCountry;

    // selected flights and bookingClasses
    private GDSFlightEntity departureDirectFlight;
    private GDSFlightEntity departureTransferFlight1;
    private GDSFlightEntity departureTransferFlight2;
    private GDSFlightEntity returnDirectFlight;
    private GDSFlightEntity returnTransferFlight1;
    private GDSFlightEntity returnTransferFlight2;
    private GDSBookingClassEntity departureDirectFlightBookingClass;
    private GDSBookingClassEntity departureTransferFlight1BookingClass;
    private GDSBookingClassEntity departureTransferFlight2BookingClass;
    private GDSBookingClassEntity returnDirectFlightBookingClass;
    private GDSBookingClassEntity returnTransferFlight1BookingClass;
    private GDSBookingClassEntity returnTransferFlight2BookingClass;

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
    private List<GDSFlightEntity> departureDirectFlightCandidates;
    private List<GDSFlightEntity> returnDirectFlightCandidates;
    private List<GDSTransferFlight> departureTransferFlightCandidates;
    private List<GDSTransferFlight> returnTransferFlightCandidates;
    
    private String title;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String country;
    private String postCode;
    private String email;
    private String contactNumber;
    private double totalPrice = 0.0;
    private String referenceNumber;

    public GDSFlightLookupManagedBean() {
    }

    public GDSAirportEntity getOriginAirport() {
        return originAirport;
    }

    public void setOriginAirport(GDSAirportEntity originAirport) {
        this.originAirport = originAirport;
    }

    public GDSAirportEntity getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(GDSAirportEntity destinationAirport) {
        this.destinationAirport = destinationAirport;
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

    public Date getReturnMinDate() {
        return returnMinDate;
    }

    public void setReturnMinDate(Date returnMinDate) {
        this.returnMinDate = returnMinDate;
    }

    public List<GDSAirportEntity> getAirportList() {
        return airportList;
    }

    public void setAirportList(List<GDSAirportEntity> airportList) {
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

    public GDSFlightEntity getDepartureDirectFlight() {
        return departureDirectFlight;
    }

    public void setDepartureDirectFlight(GDSFlightEntity departureDirectFlight) {
        this.departureDirectFlight = departureDirectFlight;
    }

    public GDSFlightEntity getDepartureTransferFlight1() {
        return departureTransferFlight1;
    }

    public void setDepartureTransferFlight1(GDSFlightEntity departureTransferFlight1) {
        this.departureTransferFlight1 = departureTransferFlight1;
    }

    public GDSFlightEntity getDepartureTransferFlight2() {
        return departureTransferFlight2;
    }

    public void setDepartureTransferFlight2(GDSFlightEntity departureTransferFlight2) {
        this.departureTransferFlight2 = departureTransferFlight2;
    }

    public GDSFlightEntity getReturnDirectFlight() {
        return returnDirectFlight;
    }

    public void setReturnDirectFlight(GDSFlightEntity returnDirectFlight) {
        this.returnDirectFlight = returnDirectFlight;
    }

    public GDSFlightEntity getReturnTransferFlight1() {
        return returnTransferFlight1;
    }

    public void setReturnTransferFlight1(GDSFlightEntity returnTransferFlight1) {
        this.returnTransferFlight1 = returnTransferFlight1;
    }

    public GDSFlightEntity getReturnTransferFlight2() {
        return returnTransferFlight2;
    }

    public void setReturnTransferFlight2(GDSFlightEntity returnTransferFlight2) {
        this.returnTransferFlight2 = returnTransferFlight2;
    }

    public GDSBookingClassEntity getDepartureDirectFlightBookingClass() {
        return departureDirectFlightBookingClass;
    }

    public void setDepartureDirectFlightBookingClass(GDSBookingClassEntity departureDirectFlightBookingClass) {
        this.departureDirectFlightBookingClass = departureDirectFlightBookingClass;
    }

    public GDSBookingClassEntity getDepartureTransferFlight1BookingClass() {
        return departureTransferFlight1BookingClass;
    }

    public void setDepartureTransferFlight1BookingClass(GDSBookingClassEntity departureTransferFlight1BookingClass) {
        this.departureTransferFlight1BookingClass = departureTransferFlight1BookingClass;
    }

    public GDSBookingClassEntity getDepartureTransferFlight2BookingClass() {
        return departureTransferFlight2BookingClass;
    }

    public void setDepartureTransferFlight2BookingClass(GDSBookingClassEntity departureTransferFlight2BookingClass) {
        this.departureTransferFlight2BookingClass = departureTransferFlight2BookingClass;
    }

    public GDSBookingClassEntity getReturnDirectFlightBookingClass() {
        return returnDirectFlightBookingClass;
    }

    public void setReturnDirectFlightBookingClass(GDSBookingClassEntity returnDirectFlightBookingClass) {
        this.returnDirectFlightBookingClass = returnDirectFlightBookingClass;
    }

    public GDSBookingClassEntity getReturnTransferFlight1BookingClass() {
        return returnTransferFlight1BookingClass;
    }

    public void setReturnTransferFlight1BookingClass(GDSBookingClassEntity returnTransferFlight1BookingClass) {
        this.returnTransferFlight1BookingClass = returnTransferFlight1BookingClass;
    }

    public GDSBookingClassEntity getReturnTransferFlight2BookingClass() {
        return returnTransferFlight2BookingClass;
    }

    public void setReturnTransferFlight2BookingClass(GDSBookingClassEntity returnTransferFlight2BookingClass) {
        this.returnTransferFlight2BookingClass = returnTransferFlight2BookingClass;
    }

    public boolean isShowTransferOptions() {
        return showTransferOptions;
    }

    public void setShowTransferOptions(boolean showTransferOptions) {
        this.showTransferOptions = showTransferOptions;
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

    public List<GDSFlightEntity> getDepartureDirectFlightCandidates() {
        return departureDirectFlightCandidates;
    }

    public void setDepartureDirectFlightCandidates(List<GDSFlightEntity> departureDirectFlightCandidates) {
        this.departureDirectFlightCandidates = departureDirectFlightCandidates;
    }

    public List<GDSFlightEntity> getReturnDirectFlightCandidates() {
        return returnDirectFlightCandidates;
    }

    public void setReturnDirectFlightCandidates(List<GDSFlightEntity> returnDirectFlightCandidates) {
        this.returnDirectFlightCandidates = returnDirectFlightCandidates;
    }

    public List<GDSTransferFlight> getDepartureTransferFlightCandidates() {
        return departureTransferFlightCandidates;
    }

    public void setDepartureTransferFlightCandidates(List<GDSTransferFlight> departureTransferFlightCandidates) {
        this.departureTransferFlightCandidates = departureTransferFlightCandidates;
    }

    public List<GDSTransferFlight> getReturnTransferFlightCandidates() {
        return returnTransferFlightCandidates;
    }

    public void setReturnTransferFlightCandidates(List<GDSTransferFlight> returnTransferFlightCandidates) {
        this.returnTransferFlightCandidates = returnTransferFlightCandidates;
    }

    @PostConstruct
    public void init() {
        fetchAllAirports();
        airportList = gDSAirportSessionBean.getAllGDSAirport();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("GDSAirportList", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("GDSAirportList", airportList);

        twoWay = true;
        Calendar today = Calendar.getInstance();
        departureMinDate = today.getTime();
        returnMinDate = today.getTime();
        activeIndex = 0;
        tab1Disabled = false;
        tab2Disabled = true;
        tab3Disabled = true;
    }
    
    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("GDSAirportList");
    }

    private void fetchAllAirports() {
        originAirportsByCountry = new ArrayList<>();
        List<String> countries = gDSAirportSessionBean.getAllCountries();
        for (String country : countries) {
            SelectItemGroup group = new SelectItemGroup(country);
            List<GDSAirportEntity> airportsInCountry = gDSAirportSessionBean.getGDSAirportInCountry(country);

            SelectItem[] selectItems = new SelectItem[airportsInCountry.size()];

            for (int i = 0; i < airportsInCountry.size(); i++) {
                GDSAirportEntity airport = airportsInCountry.get(i);
                SelectItem selectItem = new SelectItem(airport, airport.toString());
                selectItems[i] = selectItem;
            }
            group.setSelectItems(selectItems);
            originAirportsByCountry.add(group);
        }

        destinationAirportsByCountry = originAirportsByCountry;
    }

    public void onOriginChange() {
        destinationAirportsByCountry = new ArrayList<>();
        List<String> countries = gDSAirportSessionBean.getAllCountries();
        for (String country : countries) {
            SelectItemGroup group = new SelectItemGroup(country);
            List<GDSAirportEntity> airportsInCountry = gDSAirportSessionBean.getGDSAirportInCountry(country);

            SelectItem[] selectItems = new SelectItem[airportsInCountry.size()];

            for (int i = 0; i < airportsInCountry.size(); i++) {
                GDSAirportEntity airport = airportsInCountry.get(i);
                SelectItem selectItem = new SelectItem(airport, airport.toString());
                if (airport.equals(originAirport)) {
                    selectItem.setDisabled(true);
                }
                selectItems[i] = selectItem;
            }
            group.setSelectItems(selectItems);
            destinationAirportsByCountry.add(group);
        }
    }

    public void onDepartureDateSelect() {
        returnMinDate = departureDate;
    }

    public void searchFlight() {
        initSelectFlight();
        tab2Disabled = false;
        activeIndex = 1;
    }

    private void initSelectFlight() {

        departureDirectFlight = null;
        departureTransferFlight1 = null;
        departureTransferFlight2 = null;
        returnDirectFlight = null;
        returnTransferFlight1 = null;
        returnTransferFlight2 = null;

        departureHasDirectFlight = gDSFlightSessionBean.haveDirectFlight(originAirport, destinationAirport, departureDate);
        departureDirectFlightCandidates = gDSFlightSessionBean.getDirectFlights(originAirport, destinationAirport, departureDate);
        System.out.println("originAirport = " + originAirport);
        System.out.println("destinationAirport = " + destinationAirport);
        System.out.println("departureDate = " + departureDate);
        System.out.println("departureDirectFlightCandidates = " + departureDirectFlightCandidates);
        departureTransferFlightCandidates = gDSFlightSessionBean.getTransferFlightSet(originAirport, destinationAirport, departureDate);
        departureHasTransferFlight = (departureTransferFlightCandidates.size() > 0);

        if (twoWay) {
            returnHasDirectFlight = gDSFlightSessionBean.haveDirectFlight(destinationAirport, originAirport, returnDate);
            returnDirectFlightCandidates = gDSFlightSessionBean.getDirectFlights(destinationAirport, originAirport, returnDate);
            returnTransferFlightCandidates = gDSFlightSessionBean.getTransferFlightSet(destinationAirport, originAirport, returnDate);
            returnHasTransferFlight = (returnTransferFlightCandidates.size() > 0);
        }
        showTransferOptions = true;
        tab2Disabled = false;
    }

    public String getDate(Date date) {
        DateFormat df = new SimpleDateFormat("MMM d, yyyy");
        return df.format(date);
    }

    public String getUserFriendlyDuration(Date start, Date end) {
        int hourNo = (int) ((end.getTime() - start.getTime()) / 3600000);
        int minNo = (int) ((end.getTime() - start.getTime() - hourNo * 3600000) / 60000);
        return hourNo + " hour " + minNo + " mins";
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
    
    private boolean checkFlightsSubmitted() {
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
    }
    
    public void showNotes(GDSBookingClassEntity bookingClass) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bookingClass.getName(), bookingClass.getNotes());
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
    
    public boolean GDSBookingClassDisabled(GDSBookingClassEntity bc) {
        return (bc.getQuota() < (adultNo + childNo + infantNo));
    }
    
    public String GDSBookingClassPrice(GDSBookingClassEntity bc) {
        if (GDSBookingClassDisabled(bc)) {
            return "Quota not enough";
        } else {
            return "S$ " + Double.toString(CostManagementSessionBean.round(bc.getPrice(), 2));
        }
    }
    
    public void submitBookingClasses() {
        
    }
}
