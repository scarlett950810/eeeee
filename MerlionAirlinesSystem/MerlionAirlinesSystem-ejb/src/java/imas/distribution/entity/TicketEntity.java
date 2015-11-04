/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.entity;

import imas.inventory.entity.PromotionEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.SeatEntity;
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
public class TicketEntity implements Serializable {

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

    @ManyToOne
    private SeatEntity seat;

    @ManyToOne(cascade = CascadeType.MERGE)
    private FlightEntity flight;

    private String bookingClassName; // booking class Name. 
    // Can only be one of: 
    // First Class, Business Class, Premium Economy Class,
    // Full Service Economy, Economy Plus, Standard Economy, Economy Save, Economy Super Save
    // Economy Class Agency

    @ManyToOne
    private PassengerEntity passenger;
    private Double price;
    private Boolean boarded;
    private Boolean issued; // if the ticket is issued at either the check in counter or web check-in
    @ManyToOne
    private PromotionEntity promotion;

    public TicketEntity() {
    }

    public TicketEntity(FlightEntity flight, String bookingClassName, double price) {
        this.flight = flight;
        this.bookingClassName = bookingClassName;
        this.price = price;
        this.issued = false;
        this.boarded = false;
    }

    public TicketEntity(FlightEntity flight, String bookingClassName, double price, SeatEntity seat) {
        this.flight = flight;
        this.bookingClassName = bookingClassName;
        this.price = price;
        this.issued = false;
        this.boarded = false;
        this.seat = seat;
    }

    public TicketEntity(FlightEntity flight, String bookingClassName, double price, PassengerEntity passengerEntity) {
        this.flight = flight;
        this.bookingClassName = bookingClassName;
        this.price = price;
        this.issued = false;
        this.boarded = false;
        this.passenger = passengerEntity;
    }

    public TicketEntity(FlightEntity flight, String bookingClassName, double price, SeatEntity seat, PassengerEntity passengerEntity) {
        this.flight = flight;
        this.bookingClassName = bookingClassName;
        this.price = price;
        this.issued = false;
        this.boarded = false;
        this.seat = seat;
        this.passenger = passengerEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getActualBaggageWeight() {
        return actualBaggageWeight;
    }

    public void setActualBaggageWeight(Double actualBaggageWeight) {
        this.actualBaggageWeight = actualBaggageWeight;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public SeatEntity getSeat() {
        return seat;
    }

    public void setSeat(SeatEntity seat) {
        this.seat = seat;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public String getBookingClassName() {
        return bookingClassName;
    }

    public void setBookingClassName(String bookingClassName) {
        this.bookingClassName = bookingClassName;
    }

    public PassengerEntity getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEntity passenger) {
        this.passenger = passenger;
    }

    public Double getBaggageWeight() {
        return baggageWeight;
    }

    public void setBaggageWeight(Double baggageWeight) {
        this.baggageWeight = baggageWeight;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getBoarded() {
        return boarded;
    }

    public void setBoarded(Boolean boarded) {
        this.boarded = boarded;
    }

    public Boolean getIssued() {
        return issued;
    }

    public void setIssued(Boolean issued) {
        this.issued = issued;
    }

    public PromotionEntity getPromotion() {
        return promotion;
    }

    public void setPromotion(PromotionEntity promotion) {
        this.promotion = promotion;
    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
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
        if (!(object instanceof TicketEntity)) {
            return false;
        }
        TicketEntity other = (TicketEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ticket: \n" + 
                "    Flight: " + flight + "\n" +
                "    BookingClass: " + bookingClassName + "\n" +
                "    Price: " + price + "\n" +
                "    Passenger" + passenger + "\n";
    }

}
