/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Lei
 */
@Entity
public class AircraftEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String tailId;
    private Integer aircraftRange;//km
    private String aircraftType;//BOEING
    private Integer aircraftSpace;// 

    private Double cruisingSpeed;//miles/
    private Double wingSpan;//ft
    private Double aircraftWeight;//tonnes

    private Double aircraftLength;//ft
    private Double aircraftHeight;//ft
    private String powerPlant;

    private Double purchasePrice;
    private Double deprecation;
    private Double netAssetValue;

    private Double aircraftLife;
    private Double operationYear;
    private String aircraftCondition;

    //private FlightEntity flight;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "aircraftFlight")
    private List<FlightEntity> flights = new ArrayList<FlightEntity>();
    //private SeatEntity Configuration;  
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "aircraftSeats")
    private List<SeatEntity> seats = new ArrayList<SeatEntity>();
    // Hub
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private AirportEntity airportHub = new AirportEntity();
    // aircraft location

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private AirportEntity airportLocation = new AirportEntity();
    //group
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private AircraftGroupEntity aircraftGroup;

    public AircraftEntity(){
    }

    public AircraftEntity(Long id, String tailId, Integer aircraftRange, String aircraftType, Integer aircraftSpace, Double cruisingSpeed, Double wingSpan, Double aircraftWeight, Double aircraftLength, Double aircraftHeight, String powerPlant, Double purchasePrice, Double deprecation, Double netAssetValue, Double aircraftLife, Double operationYear, String aircraftCondition, AircraftGroupEntity aircraftGroup) {
        this.id = id;
        this.tailId = tailId;
        this.aircraftRange = aircraftRange;
        this.aircraftType = aircraftType;
        this.aircraftSpace = aircraftSpace;
        this.cruisingSpeed = cruisingSpeed;
        this.wingSpan = wingSpan;
        this.aircraftWeight = aircraftWeight;
        this.aircraftLength = aircraftLength;
        this.aircraftHeight = aircraftHeight;
        this.powerPlant = powerPlant;
        this.purchasePrice = purchasePrice;
        this.deprecation = deprecation;
        this.netAssetValue = netAssetValue;
        this.aircraftLife = aircraftLife;
        this.operationYear = operationYear;
        this.aircraftCondition = aircraftCondition;
        this.aircraftGroup = aircraftGroup;
    }

    public AircraftGroupEntity getAircraftGroup() {
        return aircraftGroup;
    }

    public void setAircraftGroup(AircraftGroupEntity aircraftGroup) {
        this.aircraftGroup = aircraftGroup;
    }

    public AirportEntity getAirportLocation() {
        return airportLocation;
    }

    public void setAirportLocation(AirportEntity airportLocation) {
        this.airportLocation = airportLocation;
    }

    public AirportEntity getAirportHub() {
        return airportHub;
    }

    public void setAirportHub(AirportEntity airportHub) {
        this.airportHub = airportHub;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTailId() {
        return tailId;
    }

    public void setTailId(String tailId) {
        this.tailId = tailId;
    }

    public Integer getAircraftRange() {
        return aircraftRange;
    }

    public void setAircraftRange(Integer aircraftRange) {
        this.aircraftRange = aircraftRange;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public Integer getAircraftSpace() {
        return aircraftSpace;
    }

    public void setAircraftSpace(Integer aircraftSpace) {
        this.aircraftSpace = aircraftSpace;
    }

    public Double getCruisingSpeed() {
        return cruisingSpeed;
    }

    public void setCruisingSpeed(Double cruisingSpeed) {
        this.cruisingSpeed = cruisingSpeed;
    }

    public Double getWingSpan() {
        return wingSpan;
    }

    public void setWingSpan(Double wingSpan) {
        this.wingSpan = wingSpan;
    }

    public Double getAircraftWeight() {
        return aircraftWeight;
    }

    public void setAircraftWeight(Double aircraftWeight) {
        this.aircraftWeight = aircraftWeight;
    }

    public Double getAircraftLength() {
        return aircraftLength;
    }

    public void setAircraftLength(Double aircraftLength) {
        this.aircraftLength = aircraftLength;
    }

    public Double getAircraftHeight() {
        return aircraftHeight;
    }

    public void setAircraftHeight(Double aircraftHeight) {
        this.aircraftHeight = aircraftHeight;
    }

    public String getPowerPlant() {
        return powerPlant;
    }

    public void setPowerPlant(String powerPlant) {
        this.powerPlant = powerPlant;
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

    public String getAircraftCondition() {
        return aircraftCondition;
    }

    public void setAircraftCondition(String aircraftCondition) {
        this.aircraftCondition = aircraftCondition;
    }

    public List<FlightEntity> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightEntity> flights) {
        this.flights = flights;
    }

    public List<SeatEntity> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatEntity> seats) {
        this.seats = seats;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AircraftEntity)) {
            return false;
        }
        AircraftEntity other = (AircraftEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "imas.planning.entity.AircraftEntity[ id=" + id + " ]";
    }

}
