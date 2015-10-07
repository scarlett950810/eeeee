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

    @ManyToOne
    private AircraftTypeEntity aircraftType;//BOEING

    private Double purchasePrice;
    private Double deprecation;
    private Double netAssetValue;

    private Double aircraftLife;
    private Double operationYear;
    private Double turnAroundTime; //minutes
    private String conditionDescription; // This is a string containing the description of the aircraft condition such as the left wing is abnormal
    
    //private FlightEntity flight;
    @OneToMany(mappedBy = "aircraft")
    private List<FlightEntity> flights;
    @OneToMany(mappedBy = "aircraft")
    private List<MaintenanceScheduleEntity> maintenances;
    //private SeatEntity Configuration;  
    @OneToMany(mappedBy = "aircraft", cascade = {CascadeType.ALL})
    private List<SeatEntity> seats;
    @ManyToOne
    private AirportEntity airportHub;

    // the current airport the aircraft is located or the destination if it is on the air
    @ManyToOne
    private AirportEntity currentAirport;

//group
    @ManyToOne
    private AircraftGroupEntity aircraftGroup;

    public AircraftEntity() {
    }

    public AircraftEntity(String tailId, AircraftTypeEntity aircraftType, Double purchasePrice, Double deprecation, Double netAssetValue, Double aircraftLife, Double operationYear, String conditionDescription, AirportEntity airportHub, AirportEntity currentAirport) {
        this.tailId = tailId;
        this.aircraftType = aircraftType;
        this.purchasePrice = purchasePrice;
        this.deprecation = deprecation;
        this.netAssetValue = netAssetValue;
        this.aircraftLife = aircraftLife;
        this.operationYear = operationYear;
        this.conditionDescription = conditionDescription;
        this.seats = new ArrayList();
        this.airportHub = airportHub;
        this.currentAirport = currentAirport;
    }
    
    public AircraftEntity(String tailId, AircraftTypeEntity aircraftType, Double purchasePrice, Double deprecation, Double netAssetValue, Double aircraftLife, Double operationYear, String conditionDescription, AirportEntity airportHub, AirportEntity currentAirport, Double turnAroundTime) {
        this.tailId = tailId;
        this.aircraftType = aircraftType;
        this.purchasePrice = purchasePrice;
        this.deprecation = deprecation;
        this.netAssetValue = netAssetValue;
        this.aircraftLife = aircraftLife;
        this.operationYear = operationYear;
        this.conditionDescription = conditionDescription;
        this.seats = new ArrayList();
        this.airportHub = airportHub;
        this.currentAirport = currentAirport;
        this.turnAroundTime = turnAroundTime;
    }
    
    public AircraftGroupEntity getAircraftGroup() {
        return aircraftGroup;
    }

    public void setAircraftGroup(AircraftGroupEntity aircraftGroup) {
        this.aircraftGroup = aircraftGroup;
    }

    public Double getTurnAroundTime() {
        return turnAroundTime;
    }

    public List<MaintenanceScheduleEntity> getMaintenances() {
        return maintenances;
    }

    public void setMaintenances(List<MaintenanceScheduleEntity> maintenances) {
        this.maintenances = maintenances;
    }

    public void setTurnAroundTime(Double turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public AirportEntity getCurrentAirport() {
        return currentAirport;
    }

    public void setCurrentAirport(AirportEntity currentAirport) {
        this.currentAirport = currentAirport;
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

    public String getConditionDescription() {
        return conditionDescription;
    }

    public void setConditionDescription(String conditionDescription) {
        this.conditionDescription = conditionDescription;
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

    public AircraftTypeEntity getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftTypeEntity aircraftType) {
        this.aircraftType = aircraftType;
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
        return tailId + " (" + aircraftType.toString() + ")";
    }

}
