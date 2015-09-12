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
    
    private double costPerSeatPerMile;
        private double fixedCostPerSeatPerMile;
            private double flightDistancePerSeatPerDay;
            private double fixedCostPerSeat;
                private double operatingCostPerSeat;
                private double otherCostPerSeat;
        private double flightCostPerSeatPerMile;
            private double fuelCostPerSeatPerMile;
            private double crewCostPerSeatPerMile;
            private double maintainenceCostPerSeatPerMile;
            private double tollsPerSeatPerMileForTakeoffAndlanding;
            private double otherVariableCostPerSeatPerMile;
        private double passengerCostPerSeatPerMile;
            private double showRate;
            private double averageFlightDistancePerPassenger;
            private double averageCostPerPassenger;
                private double salesCostPerPassenger;
                private double airportFeePerPassenger;
                private double checkinCostPerPassenger;
                private double mealCostPerPassenger;
                private double serviceCostPerPassenger;
                private double firstClassServiceCostPerPassenger;
                private double delayCostPerPassenger;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CostEntity() {
    }

    public double getOperatingCostPerSeat() {
        return operatingCostPerSeat;
    }

    public void setOperatingCostPerSeat(double operatingCostPerSeat) {
        this.operatingCostPerSeat = operatingCostPerSeat;
    }

    public double getOtherCostPerSeat() {
        return otherCostPerSeat;
    }

    public void setOtherCostPerSeat(double otherCostPerSeat) {
        this.otherCostPerSeat = otherCostPerSeat;
    }

    public double getFlightDistancePerSeatPerDay() {
        return flightDistancePerSeatPerDay;
    }

    public void setFlightDistancePerSeatPerDay(double flightDistancePerSeatPerDay) {
        this.flightDistancePerSeatPerDay = flightDistancePerSeatPerDay;
    }
    
    public double getFixedCostPerSeat() {
        return operatingCostPerSeat + otherCostPerSeat;
    }

    public double getFixedCostPerSeatPerMile() {
        return fixedCostPerSeat / (30 * flightDistancePerSeatPerDay);
    }

    public double getFuelCostPerSeatPerMile() {
        return fuelCostPerSeatPerMile;
    }

    public void setFuelCostPerSeatPerMile(double fuelCostPerSeatPerMile) {
        this.fuelCostPerSeatPerMile = fuelCostPerSeatPerMile;
    }

    public double getCrewCostPerSeatPerMile() {
        return crewCostPerSeatPerMile;
    }

    public void setCrewCostPerSeatPerMile(double crewCostPerSeatPerMile) {
        this.crewCostPerSeatPerMile = crewCostPerSeatPerMile;
    }

    public double getMaintainenceCostPerSeatPerMile() {
        return maintainenceCostPerSeatPerMile;
    }

    public void setMaintainenceCostPerSeatPerMile(double maintainenceCostPerSeatPerMile) {
        this.maintainenceCostPerSeatPerMile = maintainenceCostPerSeatPerMile;
    }

    public double getTollsPerSeatPerMileForTakeoffAndlanding() {
        return tollsPerSeatPerMileForTakeoffAndlanding;
    }

    public void setTollsPerSeatPerMileForTakeoffAndlanding(double tollsPerSeatPerMileForTakeoffAndlanding) {
        this.tollsPerSeatPerMileForTakeoffAndlanding = tollsPerSeatPerMileForTakeoffAndlanding;
    }

    public double getOtherVariableCostPerSeatPerMile() {
        return otherVariableCostPerSeatPerMile;
    }

    public void setOtherVariableCostPerSeatPerMile(double otherVariableCostPerSeatPerMile) {
        this.otherVariableCostPerSeatPerMile = otherVariableCostPerSeatPerMile;
    }

    public double getFlightCostPerSeatPerMile() {
        return fuelCostPerSeatPerMile + crewCostPerSeatPerMile + maintainenceCostPerSeatPerMile 
                + tollsPerSeatPerMileForTakeoffAndlanding + otherVariableCostPerSeatPerMile;
    }

    public double getSalesCostPerPassenger() {
        return salesCostPerPassenger;
    }

    public void setSalesCostPerPassenger(double salesCostPerPassenger) {
        this.salesCostPerPassenger = salesCostPerPassenger;
    }

    public double getAirportFeePerPassenger() {
        return airportFeePerPassenger;
    }

    public void setAirportFeePerPassenger(double airportFeePerPassenger) {
        this.airportFeePerPassenger = airportFeePerPassenger;
    }

    public double getCheckinCostPerPassenger() {
        return checkinCostPerPassenger;
    }

    public void setCheckinCostPerPassenger(double checkinCostPerPassenger) {
        this.checkinCostPerPassenger = checkinCostPerPassenger;
    }

    public double getMealCostPerPassenger() {
        return mealCostPerPassenger;
    }

    public void setMealCostPerPassenger(double mealCostPerPassenger) {
        this.mealCostPerPassenger = mealCostPerPassenger;
    }

    public double getServiceCostPerPassenger() {
        return serviceCostPerPassenger;
    }

    public void setServiceCostPerPassenger(double serviceCostPerPassenger) {
        this.serviceCostPerPassenger = serviceCostPerPassenger;
    }

    public double getFirstClassServiceCostPerPassenger() {
        return firstClassServiceCostPerPassenger;
    }

    public void setFirstClassServiceCostPerPassenger(double firstClassServiceCostPerPassenger) {
        this.firstClassServiceCostPerPassenger = firstClassServiceCostPerPassenger;
    }

    public double getDelayCostPerPassenger() {
        return delayCostPerPassenger;
    }

    public void setDelayCostPerPassenger(double delayCostPerPassenger) {
        this.delayCostPerPassenger = delayCostPerPassenger;
    }

    public double getAverageCostPerPassenger() {        
        return salesCostPerPassenger + airportFeePerPassenger + checkinCostPerPassenger + mealCostPerPassenger 
                + serviceCostPerPassenger + firstClassServiceCostPerPassenger + delayCostPerPassenger;
    }

    public double getPassengerCostPerSeatPerMile() {
        return averageCostPerPassenger * averageFlightDistancePerPassenger * averageCostPerPassenger;
    }

    public double getShowRate() {
        return showRate;
    }

    public void setShowRate(double showRate) {
        this.showRate = showRate;
    }

    public double getAverageFlightDistancePerPassenger() {
        return averageFlightDistancePerPassenger;
    }

    public void setAverageFlightDistancePerPassenger(double averageFlightDistancePerPassenger) {
        this.averageFlightDistancePerPassenger = averageFlightDistancePerPassenger;
    }

    public double getCostPerSeatPerMile() {
        return fixedCostPerSeatPerMile + flightCostPerSeatPerMile + passengerCostPerSeatPerMile;
    }

    public void setCostPerSeatPerMile(double costPerSeatPerMile) {
        this.costPerSeatPerMile = costPerSeatPerMile;
    }

    public void setFixedCostPerSeatPerMile(double fixedCostPerSeatPerMile) {
        this.fixedCostPerSeatPerMile = fixedCostPerSeatPerMile;
    }

    public void setFixedCostPerSeat(double fixedCostPerSeat) {
        this.fixedCostPerSeat = fixedCostPerSeat;
    }

    public void setFlightCostPerSeatPerMile(double flightCostPerSeatPerMile) {
        this.flightCostPerSeatPerMile = flightCostPerSeatPerMile;
    }

    public void setPassengerCostPerSeatPerMile(double passengerCostPerSeatPerMile) {
        this.passengerCostPerSeatPerMile = passengerCostPerSeatPerMile;
    }

    public void setAverageCostPerPassenger(double averageCostPerPassenger) {
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
