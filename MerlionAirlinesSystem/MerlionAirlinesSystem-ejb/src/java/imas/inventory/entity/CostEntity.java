/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Scarlett
 */
@Entity
public class CostEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double costPerSeatPerMile;
    private Double fixedCostPerSeatPerMile;
    private Double flightDistancePerSeatPerDay;
    private Double fixedCostPerSeat;
    private Double operatingCostPerSeat;
    private Double otherCostPerSeat;
    private Double flightCostPerSeatPerMile;
    private Double fuelCostPerSeatPerMile;
    private Double crewCostPerSeatPerMile;
    private Double maintenanceCostPerSeatPerMile;
    private Double tollsPerSeatPerMileForTakeoffAndlanding;
    private Double otherVariableCostPerSeatPerMile;
    private Double passengerCostPerSeatPerMile;
    private Double showRate;
    private Double averageFlightDistancePerPassenger;
    private Double averageCostPerPassenger;
    private Double salesCostPerPassenger;
    private Double airportFeePerPassenger;
    private Double checkinCostPerPassenger;
    private Double mealCostPerPassenger;
    private Double serviceCostPerPassenger;
    private Double firstClassServiceCostPerPassenger;
    private Double delayCostPerPassenger;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CostEntity() {
    }

    public Double getOperatingCostPerSeat() {
        return operatingCostPerSeat;
    }

    public void setOperatingCostPerSeat(Double operatingCostPerSeat) {
        this.operatingCostPerSeat = operatingCostPerSeat;
    }

    public Double getOtherCostPerSeat() {
        return otherCostPerSeat;
    }

    public void setOtherCostPerSeat(Double otherCostPerSeat) {
        this.otherCostPerSeat = otherCostPerSeat;
    }

    public Double getFlightDistancePerSeatPerDay() {
        return flightDistancePerSeatPerDay;
    }

    public void setFlightDistancePerSeatPerDay(Double flightDistancePerSeatPerDay) {
        this.flightDistancePerSeatPerDay = flightDistancePerSeatPerDay;
    }

    public Double getFixedCostPerSeat() {
        return operatingCostPerSeat + otherCostPerSeat;
    }

    public Double getFixedCostPerSeatPerMile() {
        return fixedCostPerSeat / (30 * flightDistancePerSeatPerDay);
    }

    public Double getFuelCostPerSeatPerMile() {
        return fuelCostPerSeatPerMile;
    }

    public void setFuelCostPerSeatPerMile(Double fuelCostPerSeatPerMile) {
        this.fuelCostPerSeatPerMile = fuelCostPerSeatPerMile;
    }

    public Double getCrewCostPerSeatPerMile() {
        return crewCostPerSeatPerMile;
    }

    public void setCrewCostPerSeatPerMile(Double crewCostPerSeatPerMile) {
        this.crewCostPerSeatPerMile = crewCostPerSeatPerMile;
    }

    public Double getMaintenanceCostPerSeatPerMile() {
        return maintenanceCostPerSeatPerMile;
    }

    public void setMaintenanceCostPerSeatPerMile(Double maintenanceCostPerSeatPerMile) {
        this.maintenanceCostPerSeatPerMile = maintenanceCostPerSeatPerMile;
    }

    public Double getTollsPerSeatPerMileForTakeoffAndlanding() {
        return tollsPerSeatPerMileForTakeoffAndlanding;
    }

    public void setTollsPerSeatPerMileForTakeoffAndlanding(Double tollsPerSeatPerMileForTakeoffAndlanding) {
        this.tollsPerSeatPerMileForTakeoffAndlanding = tollsPerSeatPerMileForTakeoffAndlanding;
    }

    public Double getOtherVariableCostPerSeatPerMile() {
        return otherVariableCostPerSeatPerMile;
    }

    public void setOtherVariableCostPerSeatPerMile(Double otherVariableCostPerSeatPerMile) {
        this.otherVariableCostPerSeatPerMile = otherVariableCostPerSeatPerMile;
    }

    public Double getFlightCostPerSeatPerMile() {
        return fuelCostPerSeatPerMile + crewCostPerSeatPerMile + maintenanceCostPerSeatPerMile
                + tollsPerSeatPerMileForTakeoffAndlanding + otherVariableCostPerSeatPerMile;
    }

    public Double getSalesCostPerPassenger() {
        return salesCostPerPassenger;
    }

    public void setSalesCostPerPassenger(Double salesCostPerPassenger) {
        this.salesCostPerPassenger = salesCostPerPassenger;
    }

    public Double getAirportFeePerPassenger() {
        return airportFeePerPassenger;
    }

    public void setAirportFeePerPassenger(Double airportFeePerPassenger) {
        this.airportFeePerPassenger = airportFeePerPassenger;
    }

    public Double getCheckinCostPerPassenger() {
        return checkinCostPerPassenger;
    }

    public void setCheckinCostPerPassenger(Double checkinCostPerPassenger) {
        this.checkinCostPerPassenger = checkinCostPerPassenger;
    }

    public Double getMealCostPerPassenger() {
        return mealCostPerPassenger;
    }

    public void setMealCostPerPassenger(Double mealCostPerPassenger) {
        this.mealCostPerPassenger = mealCostPerPassenger;
    }

    public Double getServiceCostPerPassenger() {
        return serviceCostPerPassenger;
    }

    public void setServiceCostPerPassenger(Double serviceCostPerPassenger) {
        this.serviceCostPerPassenger = serviceCostPerPassenger;
    }

    public Double getFirstClassServiceCostPerPassenger() {
        return firstClassServiceCostPerPassenger;
    }

    public void setFirstClassServiceCostPerPassenger(Double firstClassServiceCostPerPassenger) {
        this.firstClassServiceCostPerPassenger = firstClassServiceCostPerPassenger;
    }

    public Double getDelayCostPerPassenger() {
        return delayCostPerPassenger;
    }

    public void setDelayCostPerPassenger(Double delayCostPerPassenger) {
        this.delayCostPerPassenger = delayCostPerPassenger;
    }

    public Double getAverageCostPerPassenger() {
        return salesCostPerPassenger + airportFeePerPassenger + checkinCostPerPassenger + mealCostPerPassenger
                + serviceCostPerPassenger + firstClassServiceCostPerPassenger + delayCostPerPassenger;
    }

    public Double getPassengerCostPerSeatPerMile() {
        return averageCostPerPassenger * averageFlightDistancePerPassenger * averageCostPerPassenger;
    }

    public Double getShowRate() {
        return showRate;
    }

    public void setShowRate(Double showRate) {
        this.showRate = showRate;
    }

    public Double getAverageFlightDistancePerPassenger() {
        return averageFlightDistancePerPassenger;
    }

    public void setAverageFlightDistancePerPassenger(Double averageFlightDistancePerPassenger) {
        this.averageFlightDistancePerPassenger = averageFlightDistancePerPassenger;
    }

    public Double getCostPerSeatPerMile() {
        return fixedCostPerSeatPerMile + flightCostPerSeatPerMile + passengerCostPerSeatPerMile;
    }

    public void setCostPerSeatPerMile(Double costPerSeatPerMile) {
        this.costPerSeatPerMile = costPerSeatPerMile;
    }

    public void setFixedCostPerSeatPerMile(Double fixedCostPerSeatPerMile) {
        this.fixedCostPerSeatPerMile = fixedCostPerSeatPerMile;
    }

    public void setFixedCostPerSeat(Double fixedCostPerSeat) {
        this.fixedCostPerSeat = fixedCostPerSeat;
    }

    public void setFlightCostPerSeatPerMile(Double flightCostPerSeatPerMile) {
        this.flightCostPerSeatPerMile = flightCostPerSeatPerMile;
    }

    public void setPassengerCostPerSeatPerMile(Double passengerCostPerSeatPerMile) {
        this.passengerCostPerSeatPerMile = passengerCostPerSeatPerMile;
    }

    public void setAverageCostPerPassenger(Double averageCostPerPassenger) {
        this.averageCostPerPassenger = averageCostPerPassenger;
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
        if (!(object instanceof CostEntity)) {
            return false;
        }
        CostEntity other = (CostEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "imas.inventory.entity.CostEntity[ id=" + id + " ]";
    }

}
