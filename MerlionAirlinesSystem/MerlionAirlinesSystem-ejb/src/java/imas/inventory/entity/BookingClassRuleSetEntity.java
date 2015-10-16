/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.entity;

import imas.planning.entity.FlightEntity;
import java.io.Serializable;
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
public class BookingClassRuleSetEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private FlightEntity flight;
    private String bookingClassName;
    
    private Double changeFlightFeeForMoreThan60Days;    // percentage of origin price
    private Double changeFlightFeeForLessThan60Days;    // percentage of origin price
    private Double cancellationRefundForMoreThan60Days; // percentage of origin price
    private Double cancellationRefundForLessThan60Days; // percentage of origin price
    private Double mileageAccrual;                      // times

    public BookingClassRuleSetEntity() {
    }

    public BookingClassRuleSetEntity(String bookingClassName, Double changeFlightFeeForMoreThan60Days, Double changeFlightFeeForLessThan60Days, 
            Double cancellationRefundForMoreThan60Days, Double cancellationRefundForLessThan60Days, Double mileageAccrual) {
        this.bookingClassName = bookingClassName;
        this.changeFlightFeeForMoreThan60Days = changeFlightFeeForMoreThan60Days;
        this.changeFlightFeeForLessThan60Days = changeFlightFeeForLessThan60Days;
        this.cancellationRefundForMoreThan60Days = cancellationRefundForMoreThan60Days;
        this.cancellationRefundForLessThan60Days = cancellationRefundForLessThan60Days;
        this.mileageAccrual = mileageAccrual;
    }
    
    public BookingClassRuleSetEntity createFirstClassBookingClassRule(FlightEntity flight) {
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity("First Class", 0.0, 0.05, 1.0, 0.8, 1.5);
        bcrs.setFlight(flight);
        return bcrs;
    }
    
    public BookingClassRuleSetEntity createFirstClassAgencyBookingClassRule(FlightEntity flight) {
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity("First Class Agency", 0.0, 0.05, 1.0, 0.8, 1.5);
        bcrs.setFlight(flight);
        return bcrs;
    }
    
    public BookingClassRuleSetEntity createBusinessClassBookingClassRule(FlightEntity flight) {
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity("Business Class", 0.0, 0.05, 1.0, 0.8, 1.0);
        bcrs.setFlight(flight);
        return bcrs;
    }
    
    public BookingClassRuleSetEntity createBusinessClassAgencyBookingClassRule(FlightEntity flight) {
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity("Business Class Agency", 0.0, 0.05, 1.0, 0.8, 1.0);
        bcrs.setFlight(flight);
        return bcrs;
    }
    
    public BookingClassRuleSetEntity createPremiumEconomyClassBookingClassRule(FlightEntity flight) {
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity("Premium Economy Class", 0.05, 0.10, 0.8, 0.6, 0.5);
        bcrs.setFlight(flight);
        return bcrs;
    }
    
    public BookingClassRuleSetEntity createPremiumEconomyClassAgencyBookingClassRule(FlightEntity flight) {
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity("Premium Economy Class Agency", 0.05, 0.10, 0.8, 0.6, 0.5);
        bcrs.setFlight(flight);
        return bcrs;
    }
    
    public BookingClassRuleSetEntity createEconomyClassBookingClass1Rule(FlightEntity flight) {
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity("Full Service Economy", 0.1, 0.2, 0.6, 0.4, 0.0);
        bcrs.setFlight(flight);
        return bcrs;
    }
    
    public BookingClassRuleSetEntity createEconomyClassBookingClass2Rule(FlightEntity flight) {
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity("Economy Plus", 0.2, 0.4, 0.2, 0.2, 0.0);
        bcrs.setFlight(flight);
        return bcrs;
    }
    
    public BookingClassRuleSetEntity createEconomyClassBookingClass3Rule(FlightEntity flight) {
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity("Standard Economy", 0.4, 0.8, 0.0, 0.0, 0.0);
        bcrs.setFlight(flight);
        return bcrs;
    }
    
    public BookingClassRuleSetEntity createEconomyClassBookingClass4Rule(FlightEntity flight) {
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity("Economy Save", 0.6, 1.0, 0.0, 0.0, 0.0);
        bcrs.setFlight(flight);
        return bcrs;
    }
    
    public BookingClassRuleSetEntity createEconomyClassBookingClass5Rule(FlightEntity flight) {
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity("Economy Super Save", 1.0, 0.0, 0.0, 0.0, 0.0);
        bcrs.setFlight(flight);
        return bcrs;
    }
    
    public BookingClassRuleSetEntity createEconomyClassAgencyBookingClassRule(FlightEntity flight) {
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity("Economy Class Agency", 1.0, 1.0, 0.0, 0.0, 0.0);
        bcrs.setFlight(flight);
        return bcrs;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getChangeFlightFeeForMoreThan60Days() {
        return changeFlightFeeForMoreThan60Days;
    }

    public void setChangeFlightFeeForMoreThan60Days(Double changeFlightFeeForMoreThan60Days) {
        this.changeFlightFeeForMoreThan60Days = changeFlightFeeForMoreThan60Days;
    }

    public Double getChangeFlightFeeForLessThan60Days() {
        return changeFlightFeeForLessThan60Days;
    }

    public void setChangeFlightFeeForLessThan60Days(Double changeFlightFeeForLessThan60Days) {
        this.changeFlightFeeForLessThan60Days = changeFlightFeeForLessThan60Days;
    }

    public Double getCancellationRefundForMoreThan60Days() {
        return cancellationRefundForMoreThan60Days;
    }

    public void setCancellationRefundForMoreThan60Days(Double cancellationRefundForMoreThan60Days) {
        this.cancellationRefundForMoreThan60Days = cancellationRefundForMoreThan60Days;
    }

    public Double getCancellationRefundForLessThan60Days() {
        return cancellationRefundForLessThan60Days;
    }

    public void setCancellationRefundForLessThan60Days(Double cancellationRefundForLessThan60Days) {
        this.cancellationRefundForLessThan60Days = cancellationRefundForLessThan60Days;
    }

    public Double getMileageAccrual() {
        return mileageAccrual;
    }

    public void setMileageAccrual(Double mileageAccrual) {
        this.mileageAccrual = mileageAccrual;
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
        if (!(object instanceof BookingClassRuleSetEntity)) {
            return false;
        }
        BookingClassRuleSetEntity other = (BookingClassRuleSetEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Change flight (more than 60 days to departure): You need to pay the price difference and " + changeFlightFeeForMoreThan60Days * 100 + "% of ticket price."
                + "Change flight (less than 60 days to departure): You need to pay the price difference and " + changeFlightFeeForLessThan60Days * 100 + "% of ticket price."
                + "Cancellation (more than 60 days to departure): You would get refund of " + cancellationRefundForMoreThan60Days * 100 + "% of ticket price."
                + "Cancellation (less than 60 days to departure): You would get refund of " + cancellationRefundForLessThan60Days * 100 + "% of ticket price."
                + "Mileage Accrual: " + mileageAccrual + " times.";
    }
    
}
