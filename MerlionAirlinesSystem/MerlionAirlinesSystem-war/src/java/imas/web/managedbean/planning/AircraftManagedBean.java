/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.AircraftGroupEntity;
import imas.planning.entity.AircraftTypeEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.SeatEntity;
import imas.planning.sessionbean.AircraftSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Named;

/**
 *
 * @author Scarlett
 */
@Named(value = "aircraftManagedBean")
@Stateless
@LocalBean
public class AircraftManagedBean {

    @EJB
    private AircraftSessionBeanLocal aircraftSessionBean;

    private String tailId;
    private AircraftTypeEntity aircraftType;
    private List<AircraftTypeEntity> aircraftTypes; // list of Aircrafts to choose from
    private Double purchasePrice;
    private Double deprecation;
    private Double netAssetValue;
    private Double aircraftLife;
    private Double operationYear;
    private boolean goodCondition;
    private AirportEntity airportHub;
    private List<AirportEntity> airports; // list of Airports to choose from
    private AirportEntity currentAirport;
    private AircraftGroupEntity aircraftGroup;
    private List<AircraftGroupEntity> aircraftGroups; // list of aircraftGroups to choose from
    private List<SeatEntity> seats;
    private List<String> seatClasses;

    public AircraftManagedBean() {
        List<SeatEntity> emptySeats = new ArrayList();
        emptySeats.add(new SeatEntity());
        this.seats = emptySeats;
    }
    
    public AircraftSessionBeanLocal getAircraftSessionBean() {
        return aircraftSessionBean;
    }

    public void setAircraftSessionBean(AircraftSessionBeanLocal aircraftSessionBean) {
        this.aircraftSessionBean = aircraftSessionBean;
    }

    public String getTailId() {
        return tailId;
    }

    public void setTailId(String tailId) {
        this.tailId = tailId;
    }

    public AircraftTypeEntity getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftTypeEntity aircraftType) {
        this.aircraftType = aircraftType;
    }

    public List<AircraftTypeEntity> getAircraftTypes() {
        return aircraftTypes;
    }

    public void setAircraftTypes(List<AircraftTypeEntity> aircraftTypes) {
        this.aircraftTypes = aircraftTypes;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getDeprecation() {
        return deprecation;
    }

    public void setDeprecation(Double deprecation) {
        this.deprecation = deprecation;
    }

    public Double getNetAssetValue() {
        return netAssetValue;
    }

    public void setNetAssetValue(Double netAssetValue) {
        this.netAssetValue = netAssetValue;
    }

    public Double getAircraftLife() {
        return aircraftLife;
    }

    public void setAircraftLife(Double aircraftLife) {
        this.aircraftLife = aircraftLife;
    }

    public Double getOperationYear() {
        return operationYear;
    }

    public void setOperationYear(Double operationYear) {
        this.operationYear = operationYear;
    }

    public boolean isGoodCondition() {
        return goodCondition;
    }

    public void setGoodCondition(boolean goodCondition) {
        this.goodCondition = goodCondition;
    }

    public AirportEntity getAirportHub() {
        return airportHub;
    }

    public void setAirportHub(AirportEntity airportHub) {
        this.airportHub = airportHub;
    }

    public List<AirportEntity> getAirports() {
        System.out.print("aircraftManagedBean.getAirports called.");
        return aircraftSessionBean.getAirports();
    }

    public void setAirports(List<AirportEntity> airports) {
        this.airports = airports;
    }

    public AirportEntity getCurrentAirport() {
        return currentAirport;
    }

    public void setCurrentAirport(AirportEntity currentAirport) {
        this.currentAirport = currentAirport;
    }

    public AircraftGroupEntity getAircraftGroup() {
        return aircraftGroup;
    }

    public void setAircraftGroup(AircraftGroupEntity aircraftGroup) {
        this.aircraftGroup = aircraftGroup;
    }

    public List<AircraftGroupEntity> getAircraftGroups() {
        System.out.print("AircraftManagedBean.getAircraftGroups called.");
        return aircraftSessionBean.getAircraftGroups();
    }

    public void setAircraftGroups(List<AircraftGroupEntity> aircraftGroups) {
        this.aircraftGroups = aircraftGroups;
    }

    public List<SeatEntity> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatEntity> seats) {
        this.seats = seats;
    }

    public List<String> getSeatClasses() {
        return aircraftSessionBean.getSeatClasses();
    }

    public void setSeatClasses(List<String> seatClasses) {
        this.seatClasses = seatClasses;
    }

    public boolean addAircraft(String tailId, AircraftTypeEntity aircraftType, Double purchasePrice, Double deprecation, Double netAssetValue, Double aircraftLife, Double operationYear, String aircraftCondition, AirportEntity airportHub, AirportEntity currentAirport, AircraftGroupEntity aircraftGroup) {
        if (tailId == null | aircraftType == null | purchasePrice == null | deprecation == null | netAssetValue == null | aircraftLife == null | operationYear == null | aircraftCondition == null | airportHub == null | currentAirport == null | aircraftGroup == null) {
            return false;
        } else {
            return true;
        }

    }
    
    public boolean addAircraft(String tailId, AircraftTypeEntity aircraftType, Double purchasePrice, Double deprecation, Double netAssetValue, Double aircraftLife, Double operationYear, String aircraftCondition, List<SeatEntity> seats, AirportEntity airportHub, AirportEntity currentAirport, AircraftGroupEntity aircraftGroup) {
        if (tailId == null | aircraftType == null | purchasePrice == null | deprecation == null | netAssetValue == null | aircraftLife == null | operationYear == null | aircraftCondition == null | seats == null | airportHub == null | currentAirport == null | aircraftGroup == null) {
            return false;
        } else {
            return true;
        }

    }

}
