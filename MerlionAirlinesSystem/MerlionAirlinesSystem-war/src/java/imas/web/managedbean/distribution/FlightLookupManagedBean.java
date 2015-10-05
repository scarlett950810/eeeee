/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.distribution;

import imas.distribution.sessionbean.FlightLookupSessionBeanLocal;
import imas.planning.entity.AirportEntity;
import imas.planning.sessionbean.AircraftSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.persistence.PostRemove;
import org.primefaces.event.SelectEvent;

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

    public FlightLookupManagedBean() {
    }

    @PostConstruct
    public void init() {
        fetchAllAirports();
        airportList = aircraftSessionBean.getAirports();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("airportList", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("airportList", airportList);

//        System.out.println("airportList set in sessionMap = " + airportList);

        twoWay = true;
        Calendar today = Calendar.getInstance();
        departureMinDate = today.getTime();
        returnMinDate = today.getTime();
        today.add(Calendar.YEAR, 1);
        departureMaxDate = today.getTime();
        returnMaxDate = today.getTime();

    }

    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("airportList");
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
        }
    }

    public void submit() throws IOException {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        ec.getSessionMap().put("orginAirport", orginAirport);
        ec.getSessionMap().put("destinationAirport", destinationAirport);
        ec.getSessionMap().put("twoWay", twoWay);
        ec.getSessionMap().put("departureDate", departureDate);
        ec.getSessionMap().put("returnDate", returnDate);
        ec.getSessionMap().put("seatClass", seatClass);
        ec.getSessionMap().put("adultNo", adultNo);
        ec.getSessionMap().put("childNo", childNo);
        ec.getSessionMap().put("infantNo", infantNo);

        ec.redirect("selectFlight.xhtml");
    }

}
