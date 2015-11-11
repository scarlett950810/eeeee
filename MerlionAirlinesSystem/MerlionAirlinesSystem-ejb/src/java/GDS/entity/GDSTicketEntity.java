/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Scarlett
 */
@Entity
public class GDSTicketEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double baggageWeight;
    private Double actualBaggageWeight;
    private Boolean premiumMeal;
    private Boolean exclusiveService;
    private Boolean insurance;
    private Boolean flightWiFi;
    private String referenceNumber;
    private Double mileage;
    @ManyToOne(cascade = CascadeType.MERGE)
    private GDSFlightEntity flight;
    private String bookingClassName;
    @ManyToOne
    private GDSPassengerEntity passenger;
    private Double price;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBaggageWeight() {
        return baggageWeight;
    }

    public void setBaggageWeight(Double baggageWeight) {
        this.baggageWeight = baggageWeight;
    }

    public Double getActualBaggageWeight() {
        return actualBaggageWeight;
    }

    public void setActualBaggageWeight(Double actualBaggageWeight) {
        this.actualBaggageWeight = actualBaggageWeight;
    }

    public Boolean getPremiumMeal() {
        return premiumMeal;
    }

    public void setPremiumMeal(Boolean premiumMeal) {
        this.premiumMeal = premiumMeal;
    }

    public Boolean getExclusiveService() {
        return exclusiveService;
    }

    public void setExclusiveService(Boolean exclusiveService) {
        this.exclusiveService = exclusiveService;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(Boolean insurance) {
        this.insurance = insurance;
    }

    public Boolean getFlightWiFi() {
        return flightWiFi;
    }

    public void setFlightWiFi(Boolean flightWiFi) {
        this.flightWiFi = flightWiFi;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public GDSFlightEntity getFlight() {
        return flight;
    }

    public void setFlight(GDSFlightEntity flight) {
        this.flight = flight;
    }

    public String getBookingClassName() {
        return bookingClassName;
    }

    public void setBookingClassName(String bookingClassName) {
        this.bookingClassName = bookingClassName;
    }

    public GDSPassengerEntity getPassenger() {
        return passenger;
    }

    public void setPassenger(GDSPassengerEntity passenger) {
        this.passenger = passenger;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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
        if (!(object instanceof GDSTicketEntity)) {
            return false;
        }
        GDSTicketEntity other = (GDSTicketEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GDS.entity.GDSTicketEntity[ id=" + id + " ]";
    }
    
}
